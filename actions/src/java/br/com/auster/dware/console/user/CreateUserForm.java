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
 * Created on Oct 03, 2004
 */
package br.com.auster.dware.console.user;

import java.io.Serializable;

/**
 * @author Frederico A Ramos
 * @version $Id: CreateUserForm.java 640 2008-09-18 13:44:50Z framos $
 */
public class CreateUserForm extends UpdateUserForm implements Serializable {


    public static final String ERROR_USENAME_ALREADY_EXISTS = "text.email.exists";


    //########################################
    // instance methods
    //########################################


//    public ActionErrors validate(ActionMapping _mapping, HttpServletRequest _request) {
//
//        ActionErrors errors = super.validate(_mapping, _request);
//        try {
//			UserManager manager = (UserManager) ServiceLocator.getInstance().getService(ServiceConstants.USERMANAGER_SERVICE);
//
//            if (manager.loadUserDetails(getLogin()) != null) {
//                // if no exception was raised, this user email already exists
//                if (errors == null) {
//                    errors = new ActionErrors();
//                }
//                errors.add("login", new ActionMessage(ERROR_USENAME_ALREADY_EXISTS));
//            }
//        } catch (SecurityException se) {
//            throw new PortalRuntimeException(se);
//		}
//        return errors;
//    }


}
