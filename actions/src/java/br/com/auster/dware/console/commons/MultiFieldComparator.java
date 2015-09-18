/*
 * Copyright (c) 2004 TTI Tecnologia. All Rights Reserved.
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
 * Created on Oct 12, 2004
 */
package br.com.auster.dware.console.commons;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Comparator;

import org.apache.log4j.Logger;

import br.com.auster.common.util.I18n;

/**
 * <p>
 *              This class implements the interface <code>java.util.Comparator</code> and intends
 *      to be a fast way to enable comparison between instances of the same class. It accepts
 *  a list of instance-level variable names as comparison criteria and requires that the
 *      class implementation possesses getter methods for each one of them.
 * </p>
 * <p>
 *              Also, it can compare any primitive type and their wrapper classes, java.lang.String,
 *      java.util.Date or any other type (including instance of classes) which implements the
 *      <code>java.util.Comparable</code> interface.
 * </p>
 * <p>
 *              Attention when obfuscating the classes which might happen to have instances compared
 *      by this <code>java.util.Comparator</code> implementation, since it makes use of reflection
 *  API.
 * </p>
 *
 * @author Frederico A Ramos
 * @version $Id: MultiFieldComparator.java,v 1.1 2004/10/14 00:38:21 framos Exp $
 */
public class MultiFieldComparator implements Comparator, Serializable {


    
    //########################################
    // instance variables
    //########################################

    private String[] fieldnames;
    private int compareFactor = 1;
    

    private Logger log = Logger.getLogger(MultiFieldComparator.class);
    private I18n i18n = I18n.getInstance(MultiFieldComparator.class);


    
    //########################################
    // Constructor
    //########################################

    public MultiFieldComparator() {
        this(null);
    }
    public MultiFieldComparator(String[] _field) {
        setFieldname(_field);
    }
    public final void setFieldname(String[] _name) {
        fieldnames = _name;
    }

    public final void setDescendingOrder() {
        compareFactor = -1;
    }
    public final void setAscendingOrder() {
        compareFactor = 1;
    }

    //########################################
    // public methods
    //########################################

    /**
     * <p>
     *          Implementation of the interface method <code>compare()</code>. This method returns
     *  zero, a positive or negative integer value, depending on the comparison of the two
     *  parameters sent to it . Zero means that they are equal, a positive integer means that the
     *  first is 'higher' then the second, and an negative integer is the opposite situation.
     * </p>
     * <p>
     *          If no fields where specified or if any access or casting exception is thrown, the method
     *  will return zero, thus not modifying the list being sorted.
     * </p>
     *
     * @param arg0 the first value for comparison
     * @param arg1 the second value for comparison
     */
    public int compare(Object arg0, Object arg1) {
        if ((fieldnames == null) || (fieldnames.length <= 0)) { return 0; }
        int comparison = 0;
        try {
            for (int counter=0; fieldnames.length > counter; counter++) {
                Method getterMethod = arg0.getClass().getMethod(getGetterMethod(fieldnames[counter]), (Class[]) null);
                Comparable f0 = (Comparable) getterMethod.invoke(arg0, (Object[]) null);
                Comparable f1 = (Comparable) getterMethod.invoke(arg1, (Object[]) null);
                comparison = f0.compareTo(f1);
                if (comparison != 0) { return comparison * compareFactor; }
            }
        } catch (Exception e) {
            log.warn(i18n.getString("commons.comparator.exception", arg0.getClass(), arg1.getClass(), e.getClass() + " : " + e.getMessage()));
        }
        return comparison * compareFactor;
    }


    //########################################
    // private methods
    //########################################

    private String getGetterMethod(String _fieldname) {
        if ((_fieldname == null) || (_fieldname.length() < 1)) {
            throw new IllegalArgumentException();
        }
        return "get" + _fieldname.substring(0,1).toUpperCase() + _fieldname.substring(1);
    }


}
