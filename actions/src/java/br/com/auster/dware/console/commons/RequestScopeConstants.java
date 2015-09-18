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
public abstract class RequestScopeConstants {



    //########################################
    // static fields
    //########################################

    public static final String REQUEST_LISTOFUSERS_KEY = "users.list";
    public static final String REQUEST_USEREMAIL_KEY = "users.user.email";
    public static final String REQUEST_USERINFO_KEY = "users.user";
    public static final String REQUEST_SECURITYUSERINFO_KEY = "users.user.security";
	public static final String REQUEST_USER_CURRGROUP_KEY = "users.group.currGroup";
	public static final String REQUEST_USER_GROUPLIST_KEY = "users.group.list";

	public static final String REQUEST_USER_SELECTED_GROUP_KEY = "users.selected.group";

	public static final String REQUEST_USER_CHANGESTATUS_KEY = "status.change";

	public static final String REQUEST_USER_SELECTED_UPDATEACTION_KEY = "user.change.action";
	public static final String REQUEST_USER_SELECTED_UPDATEVALUE_KEY = "user.change.value";


	public static final String REQUEST_USER_ACTIVEGROUP_KEY = "A";
	public static final String REQUEST_USER_SUSPENDEDGROUP_KEY = "S";

	public static final String REQUEST_USER_GROUPOPERATION_KEY = "group.operation";


    public static final String REQUEST_CONFIRMOPERATION_TEXT = "page.operationText";
    public static final String REQUEST_ERROROPERATION_TEXT = "page.errorText";

    public static final String REQUEST_PAGEID_KEY = "page.paging.pageId";
    public static final String REQUEST_MOVETO_KEY = "page.paging.moveTo";
    public static final String REQUEST_TOTALPAGES_KEY = "page.paging.totalPages";
    public static final String REQUEST_OFFSET_KEY = "page.paging.offset";
    public static final String REQUEST_DISPLAYLEN_KEY = "page.paging.displayLen";

    public static final String REQUEST_ERRORFOUND_KEY = "page.error";
    public static final String REQUEST_NOTLOGGED_KEY = "page.notlogged";

    public static final String REQUEST_FILTERBY_KEY = "page.filter.field";
    public static final String REQUEST_FILTERCONDITION_KEY = "page.filter.condition";

    public static final String REQUEST_CONTINUE_KEY = "page.continue";

    public static final String REQUEST_ORDERBY_KEY = "page.order.field";
    public static final String REQUEST_ORDERWAY_KEY = "page.order.way";
    public static final String REQUEST_ORDERBACKWARD_KEY = "page.order.backward";
    public static final String REQUEST_ORDERFORWARD_KEY = "page.order.forward";

    public static final String REQUEST_LISTOFRESULTS_KEY = "requests.list";
    public static final String REQUEST_LISTOFFILES_KEY = "requests.list.files";
    public static final String REQUEST_REQID_KEY = "requests.request.id";
    public static final String REQUEST_ACCOUNTID_KEY = "requests.account.id";
    public static final String REQUEST_REQINFO_KEY = "requests.request";
    public static final String REQUEST_SECTIONINFO_KEY = "requests.section";
    public static final String REQUEST_SELECTEDFILE_KEY = "requests.file.download";

    public static final String REQUEST_UPLOADEDFILE_KEY = "requests.upload.file.name";
    public static final String REQUEST_UPLOADEDFILESIZE_KEY = "requests.upload.file.size";
    public static final String REQUEST_UPLOADFILECYCLE_KEY = "requests.upload.file.cycle";

    public static final String REQUEST_FORMATSELECTED_KEY = "requests.format.selected";

    public static final String  REQUEST_QUERYTABLE_KEY = "query.table";
    public static final String  REQUEST_QUERYFIELDLIST_KEY = "query.fields";
    public static final String  REQUEST_QUERYCONDITIONLIST_KEY = "query.conditions";
    public static final String  REQUEST_QUERYORDERLIST_KEY = "query.orderList";
    public static final String  REQUEST_QUERYINFO_KEY = "query.info";

	public static final String  REQUEST_QUERYRELATEDTABLELIST_KEY = "query.relatedTables.list";
    public static final String REQUEST_REMAKE_REPORT_ERROR_KEY = "remake.report.error";

	public static final String REQUEST_LISTOFCARRIERS_KEY = "carriers.list";
	public static final String REQUEST_CARRIER_ID_KEY = "carrier.id";
	public static final String REQUEST_CARRIER_LD_ID_KEY = "carrier.ld.id";
	public static final String REQUEST_OPERATION_DATA = "operation";
	public static final String REQUEST_OPERATION_TYPE_INSERT = "insert";
	public static final String REQUEST_OPERATION_TYPE_UPADTE = "update";
	public static final String REQUEST_OPERATION_TYPE_DELETE = "delete";
	public static final String REQUEST_OPERATION_TYPE_DETAIL = "detail";
	public static final String REQUEST_OPERATION_TYPE_DATA_FORWARD = "dataForward";

	public static final String REQUEST_LISTOFCYCLEDATES_KEY = "cycleDates.list";
	public static final String REQUEST_CYCLEDATES_ID_KEY = "cycleDates.id";

	public static final String REQUEST_LISTOFCUSTOMERTYPE_KEY = "customerType.list";
	public static final String REQUEST_CUSTOMERTYPE_ID_KEY = "customerType.id";

	public static final String REQUEST_LISTOFINVOICE_KEY = "invoice.list";
	public static final String REQUEST_INVOICE_ID_KEY = "invoice.id";

	public static final String REQUEST_LISTOFGEOGRAPHICDM_KEY = "geographic.list";
	public static final String REQUEST_GEOGRAPHICDM_ID_KEY = "geographic.id";

	public static final String REQUEST_LISTOFOCCTHRESHOLDS_KEY = "occthreshold.list";
	public static final String REQUEST_OCCTHRESHOLDS_ID_KEY = "occthreshold.id";

	public static final String REQUEST_LISTOFUSAGETHRESHOLDS_KEY = "usagethreshold.list";
	public static final String REQUEST_USAGETHRESHOLDS_ID_KEY = "usagethreshold.id";

}