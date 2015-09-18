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
 * Created on Mar 14, 2005
 */
package br.com.auster.dware.console.user.views;

import java.io.Serializable;
import java.util.Date;

/**
 * @author framos
 * @version $Id: UserView.java 640 2008-09-18 13:44:50Z framos $
 */
public class UserView implements Serializable {


    private long id;
    private String name;
    private String login;
    private Date createDate;
    private String email;
    private boolean admin;
    private String custom1;
    private String custom2;
    private String custom3;

	private String groupName;

    private boolean isLocked;



    public UserView() {
    }
    public UserView(long _id, String _name, String _email, Date _created) {
        setId(_id);
        setUserName(_name);
        setEmail(_email);
        setCreateDate(_created);
    }


    public final Date getCreateDate() {
        return createDate;
    }
    public final void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public final String getEmail() {
        return email;
    }
    public final void setEmail(String email) {
        this.email = email;
    }
    public final long getId() {
        return id;
    }
    public final void setId(long id) {
        this.id = id;
    }
    public final String getUserName() {
        return name;
    }
    public final void setUserName(String name) {
        this.name = name;
    }
    public final boolean isAdministrator() {
        return admin;
    }
    public final void setAdministrator(boolean _flag) {
        admin = _flag;
    }
    public final boolean isUserLocked() {
        return isLocked;
    }
    public final void setUserLocked(boolean _isLocked) {
        isLocked = _isLocked;
    }
	public final String getGroupName() {
		return groupName;
	}
	public final void setGroupName(String _name) {
		groupName = _name;
	}

	public final String getLogin() {
		return this.login;
	}
	public final void setLogin(String _login) {
		this.login = _login;
	}

	public final String getCustom1() { return this.custom1; }
	public final void setCustom1(String _info) { this.custom1 = _info; }

	public final String getCustom2() { return this.custom2; }
	public final void setCustom2(String _info) { this.custom2 = _info; }

	public final String getCustom3() { return this.custom3; }
	public final void setCustom3(String _info) { this.custom3 = _info; }

}
