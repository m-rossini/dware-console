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
 * Created on May 3, 2005
 */
package br.com.auster.dware.console.queries.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import br.com.auster.facelift.queries.model.ColumnObject;
import br.com.auster.facelift.queries.model.ViewObject;

public abstract class ViewFactory {

    
    
    public static List createTablesList(Collection _viewsList) {
        if (_viewsList == null) {
            return null;
        }
        ArrayList resultList = new ArrayList(_viewsList.size());
        Iterator iterator = _viewsList.iterator();
        while (iterator.hasNext()) {
            resultList.add(createTableView( (ViewObject)iterator.next() ));
        }
        return resultList;
    } 
    
    
    public static TableView createTableView(ViewObject _view) {
        TableView view = new TableView();
        view.setName(_view.getName());
        view.setDisplayName(_view.getDisplayCaption());
        Iterator iterator = _view.getColumns().iterator();
        while (iterator.hasNext()) {
            ColumnObject colView = (ColumnObject)iterator.next();
			if (! colView.isVisible()) {
				continue;
			}
            view.addColumn(colView.getQualifiedName(), colView.getDisplayCaption());
        }
        return view;
    }
    
}
