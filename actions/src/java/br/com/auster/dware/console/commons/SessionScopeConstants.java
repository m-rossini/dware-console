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
 * Created on Sep 5, 2004
 */
package br.com.auster.dware.console.commons;

/**
 * @author Frederico A Ramos
 * @version $Id: Constants.java,v 1.9 2004/10/21 18:50:16 framos Exp $
 */
public abstract class SessionScopeConstants {



    //########################################
    // static fields
    //########################################

    public static final String SESSION_USERINFO_KEY = "session.user";
//    public static final String SESSION_USERSECURITY_KEY = "session.security";

    public static final String SESSION_LISTOFRESULTS_KEY = "session.resultList";
    public static final String SESSION_CACHEDACCOUNTS_KEY = "session.accounts.selectedList";

    public static final String SESSION_NEWREQUEST_KEY = "session.request.new";

    public static final String SESSION_UPLOADEDFILE_KEY = "session.uploaded.file";
    public static final String SESSION_SELECTEDACCOUNTS_ERROR_KEY = "session.uploaded.accountError";

    public static final String SESSION_CARRIERINFO_KEY = "session.carrier";
}
