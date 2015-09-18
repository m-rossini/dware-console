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
 * Created on Jul 25, 2005
 */
package br.com.auster.dware.console.user.views;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author framos
 * @version $Id: PermissionGroupView.java 640 2008-09-18 13:44:50Z framos $
 */
public class PermissionGroupView implements Serializable {


	private String groupName;
	private long groupId;
	private boolean status;

	private Map permissions;



	public PermissionGroupView() {
		permissions = new HashMap();
	}

	public PermissionGroupView(int _id, String _name, boolean _status, Map _permissions) {
		setGroupId(_id);
		setGroupName(_name);
		setStatus(_status);
		setPermissions(_permissions);
	}

	public final String getGroupId() {
		return String.valueOf(groupId);
	}
	public final void setGroupId(long _id) {
		groupId = _id;
	}
	public final String getGroupName() {
		return groupName;
	}
	public final void setGroupName(String _name) {
		groupName = _name;
	}
	public final Map getPermissions() {
		return permissions;
	}
	public final void setPermissions(Map _permissions) {
		permissions = _permissions;
	}
	public final boolean getStatus() {
		return status;
	}
	public final void setStatus(boolean _status) {
		status = _status;
	}
}
