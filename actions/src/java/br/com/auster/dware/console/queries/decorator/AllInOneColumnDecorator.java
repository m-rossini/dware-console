/*
 * Copyright (c) 2004 Auster Solutions. All Rights Reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 * Created on 01/12/2005
 */
package br.com.auster.dware.console.queries.decorator;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.displaytag.decorator.ColumnDecorator;
import org.displaytag.exception.DecoratorException;

import br.com.auster.common.io.IOUtils;
import br.com.auster.common.log.LogFactory;

/**
 * @author framos
 * @version $Id$
 */
public class AllInOneColumnDecorator implements ColumnDecorator {

	
	
    //########################################
    // instance variables
    //########################################
	
	private ThreadLocal dateFormatter; 
	private ThreadLocal timeFormatter;
	private ThreadLocal datetimeFormatter;
	private ThreadLocal fpFormatter;
	private ThreadLocal numberFormatter;

	

    //########################################
    // class variables
    //########################################
	
	private static final Logger log = LogFactory.getLogger(AllInOneColumnDecorator.class);
	

	
    //########################################
    // class constants
    //########################################
	
	public static final String DEFAULT_DECORATOR_DATETIME_PATTERN = "dd-MM-yyyy HH:mm:ss";
	public static final String DEFAULT_DECORATOR_DATE_PATTERN = "dd-MM-yyyy";
	public static final String DEFAULT_DECORATOR_TIME_PATTERN = "HH:mm:ss";
	public static final String DEFAULT_DECORATOR_FLOAT_PATTERN = "#,##0.0000;-#,##0.0000";
	public static final String DEFAULT_DECORATOR_INT_PATTERN = "#,##0;-#,##0";
	
	
	public static final String DEFAULT_DECORATOR_DATETIME_KEY = "datetime";
	public static final String DEFAULT_DECORATOR_DATE_KEY = "date";
	public static final String DEFAULT_DECORATOR_TIME_KEY = "time";
	public static final String DEFAULT_DECORATOR_FLOAT_KEY = "float";
	public static final String DEFAULT_DECORATOR_INT_KEY = "int";
	
	
	
    //########################################
    // constructor
    //########################################
	
	public AllInOneColumnDecorator() {
		
		Properties p = new Properties();
		p.put(DEFAULT_DECORATOR_DATETIME_KEY, DEFAULT_DECORATOR_DATETIME_PATTERN);
		p.put(DEFAULT_DECORATOR_DATE_KEY, DEFAULT_DECORATOR_DATE_PATTERN);
		p.put(DEFAULT_DECORATOR_TIME_KEY, DEFAULT_DECORATOR_TIME_PATTERN);
		p.put(DEFAULT_DECORATOR_FLOAT_KEY, DEFAULT_DECORATOR_FLOAT_PATTERN);
		p.put(DEFAULT_DECORATOR_INT_KEY, DEFAULT_DECORATOR_INT_PATTERN);
		try {
			p.load(IOUtils.openFileForRead("decorator.patterns"));
		} catch (IOException ioe) {
			log.warn("could not load properties file for decorator... defaulting format patterns.");
		}
		dateFormatter = new ThreadLocal();
		dateFormatter.set(new SimpleDateFormat((String)p.get(DEFAULT_DECORATOR_DATE_KEY)));
		timeFormatter = new ThreadLocal();
		timeFormatter.set(new SimpleDateFormat((String)p.get(DEFAULT_DECORATOR_TIME_KEY)));
		datetimeFormatter = new ThreadLocal();
		datetimeFormatter.set(new SimpleDateFormat((String)p.get(DEFAULT_DECORATOR_DATETIME_KEY)));
		numberFormatter = new ThreadLocal();
		numberFormatter.set(new DecimalFormat((String)p.get(DEFAULT_DECORATOR_INT_KEY)));
		fpFormatter = new ThreadLocal();
		fpFormatter.set(new DecimalFormat((String)p.get(DEFAULT_DECORATOR_FLOAT_KEY)));
	}

	
	
    //########################################
    // public methods
    //########################################
	
	/**
	 * @see org.displaytag.decorator.ColumnDecorator#decorate(java.lang.Object)
	 */
	public String decorate(Object _value) throws DecoratorException {
		
		if (_value == null) { return null; }
		
		// if is a floating-point number
		if ((_value instanceof Double) || (_value instanceof Float) || (_value instanceof BigDecimal)) {
			return ((DecimalFormat)fpFormatter.get()).format(_value);
		// if is a non floating-point number 
		} else if (_value instanceof Number) {
			return ((DecimalFormat)numberFormatter.get()).format(_value);
		// if has only date information
		} else if (_value instanceof java.sql.Date) {
			return ((SimpleDateFormat)dateFormatter.get()).format(_value);
		// if has only time information
		} else if (_value instanceof java.sql.Time) {
			return ((SimpleDateFormat)timeFormatter.get()).format(_value);
		// if has date & time information
		} else if (_value instanceof java.util.Date) {
			return ((SimpleDateFormat)datetimeFormatter.get()).format(_value);
		}
		return _value.toString();
	}

}
