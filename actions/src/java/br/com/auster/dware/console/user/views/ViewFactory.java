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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.com.auster.security.model.DomainRoleRelation;
import br.com.auster.security.model.Role;
import br.com.auster.security.model.User;


/**
 * @author framos
 * @version $Id: ViewFactory.java 640 2008-09-18 13:44:50Z framos $
 */
public abstract class ViewFactory {


    public static List createUserList(Collection _list) {
        if (_list != null) {
            Iterator iterator = _list.iterator();
            LinkedList newList = new LinkedList();
            while (iterator.hasNext()) {
                newList.add( createUser((User) iterator.next()) );
            }
            return newList;
        }
        return Collections.EMPTY_LIST;
    }

    public static UserView createUser(User _user) {
        UserView view = new UserView();
        view.setId(_user.getUid());
        view.setEmail(_user.getEmail());
        view.setUserLocked(_user.isLocked());
        view.setUserName(_user.getFullName());
        view.setLogin(_user.getLogin());
        view.setCustom1(_user.getCustom1());
        view.setCustom2(_user.getCustom2());
        view.setCustom3(_user.getCustom3());
        return view;
    }

    public static List createGroups(Collection _groupList) {
    	return createGroups(_groupList, false);
    }
	public static List createGroups(Collection _groupList, boolean _hideDisabled) {
		if (_groupList != null) {
			Iterator iterator = _groupList.iterator();
			LinkedList result = new LinkedList();
			while (iterator.hasNext()) {
				Role vo = (Role) iterator.next();
				if (_hideDisabled && (!vo.isActive())) { continue; }
				PermissionGroupView g = new PermissionGroupView();
				g.setGroupId(vo.getUid());
				g.setGroupName(vo.getRoleName());
				g.setStatus(vo.isActive());
				result.add(g);
			}
			return result;
		}
		return Collections.EMPTY_LIST;
	}

	public static void setPermissions(PermissionGroupView _group, Collection _permissions) {
		// add permissions
		if ((_permissions == null) || (_group == null)) { return; }
		Map perms = new HashMap();
		for (Iterator it=_permissions.iterator(); it.hasNext();) {
			DomainRoleRelation permVo = (DomainRoleRelation) it.next();
			perms.put(permVo.getDomainName(), "true");
		}
		_group.setPermissions(perms);
	}
}
