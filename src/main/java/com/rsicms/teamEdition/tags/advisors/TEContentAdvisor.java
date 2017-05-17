package com.rsicms.teamEdition.tags.advisors;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.ObjectType;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.content.ContentAdvisorContext;
import com.reallysi.rsuite.api.content.ContentDisplayAdvisor;
import com.reallysi.rsuite.api.content.ContentDisplayObject;
import com.rsicms.teamEdition.tags.utils.TagsUtils;

public class TEContentAdvisor implements ContentDisplayAdvisor {

    @Override
    public void adjustContentItem(ContentAdvisorContext context, ContentDisplayObject item) throws RSuiteException {
        User user = context.getUser();
        if (user == null) {
            user = context.getAuthorizationService().getSystemUser();
        }
        ManagedObject mo = null;
    	try {
    		mo = item.getManagedObject();
            ObjectType objType = mo.getObjectType();
            if (objType == ObjectType.MANAGED_OBJECT_REF || objType == ObjectType.CONTENT_ASSEMBLY_REF) {
	            mo = context.getManagedObjectService().getManagedObject(user, mo.getTargetId());
	            objType = mo.getObjectType(); 
            }
        } catch (Exception e) {
        	//Likely caching issue; mo doesn't exist.
        	return;
        }

        List<String> tags = TagsUtils.tagList(context, user, mo);

        if (TagsUtils.tagInColumn(context)) {
        	item.addCustomValue("tags", StringUtils.join(tags, ", "));
        } else {
	        String label = mo.getDisplayName();
	        if (tags.size() > 0) {
	            label = label + " <span class=\"rsuiteTagLabel\" style=\"padding-left:10px; color:green\">" + StringUtils.join(tags, ", ") + "</span>";
	        }
	        item.setLabel(label);
        }
    }

    @Override
    public void adjustNodeCollectionList(ContentAdvisorContext context, List<ContentDisplayObject> collection) throws RSuiteException {
    }

}
