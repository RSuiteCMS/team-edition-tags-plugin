package com.rsicms.teamEdition.tags.formhandlers;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.forms.DefaultFormHandler;
import com.reallysi.rsuite.api.forms.FormColumnInstance;
import com.reallysi.rsuite.api.forms.FormDefinition;
import com.reallysi.rsuite.api.forms.FormInstance;
import com.reallysi.rsuite.api.forms.FormInstanceCreationContext;
import com.reallysi.rsuite.api.forms.FormParameterInstance;
import com.rsicms.rsuite.helpers.utils.RSuiteUtils;
import com.rsicms.teamEdition.tags.utils.TagsUtils;

/**
 * Populate the tags form to have 3 new empty fields availablew whenever open 
 * 
 */
public class TagsFormHandler extends DefaultFormHandler {
    private static Log log = LogFactory.getLog(TagsFormHandler.class);

    public void initialize(FormDefinition formDefinition) {
    }

    @Override
    public void adjustFormInstance(FormInstanceCreationContext context, FormInstance formInstance) throws RSuiteException {
        User user = context.getUser();
    	String moid = context.getArgs().getFirstValue("rsuiteId");
        ManagedObject mo = context.getManagedObjectService().getManagedObject(user, moid);
        mo = RSuiteUtils.getRealMo(context, user, mo);

        List<String> tags = TagsUtils.tagList(context, user, mo);
        
        FormColumnInstance col1 = formInstance.getColumns().get(0);
        FormParameterInstance tagParam = col1.getParams().get(0);
        for (String value : tags) {
            tagParam.addValue(value);
        }
        tagParam.addValue("");
        tagParam.addValue("");
        tagParam.addValue("");
    }

}
