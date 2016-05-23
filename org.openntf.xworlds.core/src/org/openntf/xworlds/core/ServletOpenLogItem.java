package org.openntf.xworlds.core;

import javax.servlet.ServletContext;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.RichTextItem;
import org.openntf.domino.logging.BaseOpenLogItem;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.XotsContext;
import org.openntf.xworlds.xots.XotsUtil;

import lotus.domino.NotesException;

public class ServletOpenLogItem extends BaseOpenLogItem {

	private String servletPath;
	private XotsContext context;

	public ServletOpenLogItem(ServletContext ctx) {
		setContext(XotsUtil.initialiseXotsContextFromServlet(ctx));
	}

	public ServletOpenLogItem(XotsContext ctx) {
		setContext(ctx);
	}

	public XotsContext getContext() {
		return context;
	}

	public void setContext(XotsContext context) {
		this.context = context;
	}

	@Override
	public void setCurrentDatabase(final Database db) {
		throw new UnsupportedOperationException("Current database should not be set from a CrossWorlds application");
	}

	public void setCurrentDatatbase() {
		throw new UnsupportedOperationException("Current database should not be set from a CrossWorlds application");
	}

	@Override
	public Database getCurrentDatabase() {
		throw new UnsupportedOperationException("Current database should not be set from a CrossWorlds application");
	}

	@Override
	public String getCurrentDatabasePath() {
		throw new UnsupportedOperationException("Current database should not be set from a CrossWorlds application");
	}

	public String getServletPath() {
		return servletPath;
	}

	public void setServletPath(String servletPath) {
		this.servletPath = servletPath;
	}

	@Override
	public String getLogDbName() {
		return getContext().getOpenLogApiPath();
	}

	@Override
	public boolean writeToLog() {
		reinitialiseSettings();

		// exit early if there is no database
		Database db = getLogDb();
		if (db == null) {
			return false;
		}

		boolean retval = false;
		Document logDoc = null;
		RichTextItem rtitem = null;
		Database docDb = null;

		try {
			logDoc = db.createDocument();

			logDoc.appendItemValue("Form", _logFormName);

			Throwable ee = getBase();
			if (ee != null) {
				StackTraceElement ste = ee.getStackTrace()[0];
				if (ee instanceof NotesException) {
					logDoc.replaceItemValue("LogErrorNumber", ((NotesException) ee).id);
					logDoc.replaceItemValue("LogErrorMessage", ((NotesException) ee).text);
				} else {
					// Fixed next line
					logDoc.replaceItemValue("LogErrorMessage", getMessage());
				}
				logDoc.replaceItemValue("LogErrorLine", ste.getLineNumber());
				logDoc.replaceItemValue("LogFromAgent", ste.getClassName());
				logDoc.replaceItemValue("LogFromMethod", ste.getMethodName());
			}

			if (LogType.TYPE_EVENT.getValue().equals(getEventType())) {
				if (!getSuppressEventStack()) {
					logDoc.replaceItemValue("LogStackTrace", getStackTrace(ee));
				}
			} else {
				logDoc.replaceItemValue("LogStackTrace", getStackTrace(ee));
			}
			logDoc.replaceItemValue("LogSeverity", getSeverity().getName());
			logDoc.replaceItemValue("LogEventTime", getEventTime());
			logDoc.replaceItemValue("LogEventType", getEventType());
			logDoc.replaceItemValue("LogMessage", getMessage());
			logDoc.replaceItemValue("LogFromDatabase", getContext().getContextApiPath());
			logDoc.replaceItemValue("LogFromServer", getThisServer());
			// Fixed next line
			logDoc.replaceItemValue("LogAgentLanguage", "Java");
			logDoc.replaceItemValue("LogUserName", Factory.getSession(SessionType.CURRENT).getUserName());
			logDoc.replaceItemValue("LogEffectiveName", Factory.getSession(SessionType.CURRENT).getEffectiveUserName());
			logDoc.replaceItemValue("LogAccessLevel", getAccessLevel());
			logDoc.replaceItemValue("LogUserRoles", getUserRoles());
			logDoc.replaceItemValue("LogClientVersion", getClientVersion());
			logDoc.replaceItemValue("LogAgentStartTime", getStartTime());

			if (getErrDoc() != null) {
				docDb = getErrDoc().getParentDatabase();
				rtitem = logDoc.createRichTextItem("LogDocInfo");
				rtitem.appendText("The document associated with this event is:");
				rtitem.addNewLine(1);
				rtitem.appendText("Server: " + docDb.getServer());
				rtitem.addNewLine(1);
				rtitem.appendText("Database: " + docDb.getFilePath());
				rtitem.addNewLine(1);
				rtitem.appendText("UNID: " + getErrDoc().getUniversalID());
				rtitem.addNewLine(1);
				rtitem.appendText("Note ID: " + getErrDoc().getNoteID());
				rtitem.addNewLine(1);
				rtitem.appendText("DocLink: ");
				rtitem.appendDocLink(_errDoc, getErrDoc().getUniversalID());
			}

			// make sure Depositor-level users can add documents too
			logDoc.appendItemValue("$PublicAccess", "1");

			logDoc.save(true);
			retval = true;
		} catch (Exception e) {
			debugPrint(e);
			retval = false;
		}

		return retval;
	}

	@Override
	public void reinitialiseSettings() {
		servletPath = "";
		super.reinitialiseSettings();
	}

}
