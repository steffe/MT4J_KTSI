/***********************************************************************
*   MT4j Copyright (c) 2008 - 2012, C.Ruff, Fraunhofer-Gesellschaft All rights reserved.
*
*   This file is part of MT4j.
*
*   MT4j is free software: you can redistribute it and/or modify
*   it under the terms of the GNU Lesser General Public License as published by
*   the Free Software Foundation, either version 3 of the License, or
*   (at your option) any later version.
*
*   MT4j is distributed in the hope that it will be useful,
*   but WITHOUT ANY WARRANTY; without even the implied warranty of
*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
*   GNU Lesser General Public License for more details.
*
*   You should have received a copy of the GNU Lesser General Public License
*   along with MT4j.  If not, see <http://www.gnu.org/licenses/>.
*
************************************************************************/
package org.mt4j.util.logging;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

/**
 * The Class Log4jLogger.
 */
public class Log4jLogger implements ILogger {
	
	/** The logger. */
	private Logger logger;
	
	/**
	 * Instantiates a new log4j logger.
	 */
	public Log4jLogger(){	}
	
	/**
	 * Instantiates a new log4j logger.
	 *
	 * @param name the name
	 */
	private Log4jLogger(String name){
		this.logger = Logger.getLogger(name);
		//System.out.println("Created logger: " + logger);
		SimpleLayout l = new SimpleLayout();
		ConsoleAppender ca = new ConsoleAppender(l);
		logger.addAppender(ca);
	}

	/* (non-Javadoc)
	 * @see org.mt4j.util.logging.ILogger#setLevel(int)
	 */
	public void setLevel(int level) {
		switch (level) {
		case OFF:
			this.logger.setLevel(Level.OFF); 
			break;
		case ALL:
			this.logger.setLevel(Level.ALL); 
			break;
		case INFO:
			this.logger.setLevel(Level.INFO); 
			break;
		case DEBUG:
			this.logger.setLevel(Level.DEBUG); 
			break;
		case WARN:
			this.logger.setLevel(Level.WARN); 
			break;
		case ERROR:
			this.logger.setLevel(Level.ERROR); 
			break;
		default:
			break;
		}
	}

	/* (non-Javadoc)
	 * @see org.mt4j.util.logging.ILogger#info(java.lang.Object)
	 */
	public void info(Object message) {
		this.logger.info(message);
	}

	/* (non-Javadoc)
	 * @see org.mt4j.util.logging.ILogger#debug(java.lang.Object)
	 */
	public void debug(Object message) {
		this.logger.debug(message);
	}

	/* (non-Javadoc)
	 * @see org.mt4j.util.logging.ILogger#warn(java.lang.Object)
	 */
	public void warn(Object message) {
		this.logger.warn(message);
	}

	/* (non-Javadoc)
	 * @see org.mt4j.util.logging.ILogger#error(java.lang.Object)
	 */
	public void error(Object message) {
		this.logger.error(message);
	}

//	public ILogger getLogger(String name) {
//		this.logger = Logger.getLogger(name);
////		Logger logger = Logger.getLogger(name);
//		System.out.println("Created logger: " + logger);
//		SimpleLayout l = new SimpleLayout();
//		ConsoleAppender ca = new ConsoleAppender(l);
//		logger.addAppender(ca);
////		return new Log4jLogger(logger);
//		return this;
//	}
	
	/* (non-Javadoc)
 * @see org.mt4j.util.logging.ILogger#createNew(java.lang.String)
 */
public ILogger createNew(String name) {
		return new Log4jLogger(name);
	}
	

	/* (non-Javadoc)
	 * @see org.mt4j.util.logging.ILogger#getLevel()
	 */
	public int getLevel() {
		Level level = this.logger.getLevel();
		if (level.equals(Level.OFF)){
			return ILogger.OFF;
		}else if (level.equals(Level.ALL)){
			return ILogger.ALL;
		}else if (level.equals(Level.INFO)){
			return ILogger.INFO;
		}else if (level.equals(Level.DEBUG)){
			return ILogger.DEBUG;
		}else if (level.equals(Level.WARN)){
			return ILogger.WARN;
		}else if (level.equals(Level.ERROR)){
			return ILogger.ERROR;
		}else{
			return -1;
		}
	}
	

}
