<%

try {

	br.com.auster.billcheckout.portal.ejb.logic.request.interfaces.RequestManagerHome home =
				br.com.auster.billcheckout.portal.ejb.logic.request.session.RequestManagerUtil.getHome();
	br.com.auster.billcheckout.portal.ejb.logic.request.interfaces.RequestManager manager = home.create();
	
	br.com.auster.billcheckout.portal.ejb.logic.request.interfaces.filters.RequestFilter filter = 
		new br.com.auster.billcheckout.portal.ejb.logic.request.interfaces.filters.RequestFilter();
		
	filter.setRequestId(50);

	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd HHmmSSSS");

	System.out.println(manager.hasRequestFinished(filter));
	
	filter.setAccountId("0002553123");
	System.out.println(manager.hasAccountFinished(filter));
	
	
} catch (Exception e) {
	e.printStackTrace();
}


%>