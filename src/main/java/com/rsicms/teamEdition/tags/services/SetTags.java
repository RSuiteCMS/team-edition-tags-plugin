package com.rsicms.teamEdition.tags.services;

import java.util.ArrayList;
import java.util.List;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.NotificationResult;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.rsuite.helpers.utils.RSuiteUtils;
import com.rsicms.teamEdition.tags.utils.TagsUtils;

/**
 * Set values for RSuite Tags from form
 *
 */
public class SetTags extends DefaultRemoteApiHandler {

	public RemoteApiResult execute(RemoteApiExecutionContext context, CallArgumentList args) throws RSuiteException {

		User user = context.getSession().getUser();
		ManagedObjectService moSvc = context.getManagedObjectService();
		ManagedObject mo = moSvc.getManagedObject(user, args.getFirstString("rsuiteId"));
		mo = RSuiteUtils.getRealMo(context, user, mo);

		String lmdName = TagsUtils.tagLmdName(context);
		TagsUtils.createLmdFieldIfDoesntExist(context, lmdName, false, true, false);
		TagsUtils.addElementToLmdIfNotAlready(context, lmdName, null, mo.getLocalName());

		List<String> tags = args.getStrings("tags");

		List<MetaDataItem> mdis = mo.getMetaDataItems();
		for (MetaDataItem mdi : mdis) {
			if (mdi.getName().equals(lmdName)) {
				if (tags.contains(mdi.getValue())) {
					tags.remove(mdi.getValue());
				} else {
					context.getManagedObjectService().removeMetaDataEntry(user, mo.getId(), mdi);
				}
			}
		}
		List<MetaDataItem> newMdis = new ArrayList<MetaDataItem>();
		for (String value : tags) {
			if (!value.trim().isEmpty()) {
				newMdis.add(new MetaDataItem(lmdName, value.trim()));
			}
		}
		context.getManagedObjectService().addMetaDataEntries(user, mo.getId(), newMdis);

		String title = context.getMessageResources().getMessageText("tags/notification/noticeTitle");
		String text = context.getMessageResources().getMessageText("tags/notification/noticeText");
		return new NotificationResult(title, text);
	}

}