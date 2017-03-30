package com.rsicms.teamEdition.tags.advisors;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.ObjectType;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.content.ContentAdvisorContext;
import com.reallysi.rsuite.api.content.ContentDisplayAdvisor;
import com.reallysi.rsuite.api.content.ContentDisplayObject;

public class TEContentAdvisor implements ContentDisplayAdvisor {

    @Override
    public void adjustContentItem(ContentAdvisorContext context, ContentDisplayObject item) throws RSuiteException {
        Log log = LogFactory.getLog(TEContentAdvisor.class);

        User user = context.getUser();
        if (user == null) {
            user = context.getAuthorizationService().getSystemUser();
        }

        ManagedObject mo = item.getManagedObject();
        ObjectType objType = mo.getObjectType();
        if (objType == ObjectType.MANAGED_OBJECT_REF || objType == ObjectType.CONTENT_ASSEMBLY_REF) {
            mo = context.getManagedObjectService().getManagedObject(user, mo.getTargetId());
            objType = mo.getObjectType();
        }

        List<MetaDataItem> mdItems = mo.getMetaDataItems();
        List<String> tags = new ArrayList<String>();
        for (MetaDataItem mdItem : mdItems) {
            if (mdItem.getName().equals("Tag")) {
                tags.add(mdItem.getValue());
            }
        }

        String label = mo.getDisplayName();
        if (tags.size() > 0) {
            label = label + " <span style=\"padding-left:10px; color:green\">" + StringUtils.join(tags, ", ") + "</span>";
        }
        item.setLabel(label);
    }

    @Override
    public void adjustNodeCollectionList(ContentAdvisorContext context, List<ContentDisplayObject> collection) throws RSuiteException {
    }

}
