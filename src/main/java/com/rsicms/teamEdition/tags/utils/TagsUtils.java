package com.rsicms.teamEdition.tags.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.reallysi.rsuite.api.DataType;
import com.reallysi.rsuite.api.ElementMatchingCriteria;
import com.reallysi.rsuite.api.ElementMatchingOptions;
import com.reallysi.rsuite.api.FormControlType;
import com.reallysi.rsuite.api.LayeredMetadataDefinition;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.MetaDataService;

/**
 * Collection of methods useful for working with RSuite tags.
 */
public class TagsUtils {


	/**
	 * @param context
	 * @param user
	 * @param mo
	 * @return
	 * @throws RSuiteException
	 */
	public static List<String> tagList(ExecutionContext context, User user, ManagedObject mo) throws RSuiteException {
        List<MetaDataItem> mdItems = mo.getMetaDataItems();
        List<String> tags = new ArrayList<String>();
        for (MetaDataItem mdItem : mdItems) {
            if (mdItem.getName().equals(TagsUtils.tagLmdName(context))) {
                tags.add(mdItem.getValue());
            }
        }
        Collections.sort(tags, String.CASE_INSENSITIVE_ORDER);
        return tags;
	}
	
	
	/**
	 * @param context
	 * @return
	 */
	public static String tagLmdName(ExecutionContext context) {
		String tagLMD = context.getConfigurationProperties().getProperty("rsuite.lmd.tag", "RSuiteTag");
		return tagLMD;
	}

	/**
	 * @param context
	 * @return
	 */
	public static Boolean tagInColumn(ExecutionContext context) {
		Boolean tagInColumn = Boolean.parseBoolean(context.getConfigurationProperties().getProperty("rsuite.lmd.tag.column", "false"));
		return tagInColumn;
	}

    /**
     * @param context
     * @param lmdName
     * @param isVersionable
     * @param allowsMultiple
     * @param allowsContextual
     * @throws RSuiteException
     */
	public static void createLmdFieldIfDoesntExist(ExecutionContext context, String lmdName, Boolean isVersionable, Boolean allowsMultiple,
            Boolean allowsContextual) throws RSuiteException {

        MetaDataService metaSvc = context.getMetaDataService();
        User user = context.getAuthorizationService().getSystemUser();

        LayeredMetadataDefinition def = metaSvc.getLayeredMetaDataDefinition(user, lmdName);
        if (def == null) {
            DataType dt = null;
            List<ElementMatchingCriteria> elemSet = null;
            metaSvc.createLayeredMetaDataDefinition(user, lmdName, "string", isVersionable, allowsMultiple, allowsContextual, elemSet, dt, null, null, FormControlType.INPUT, null);
        }

    }

    /**
     * @param context
     * @param lmdName
     * @param namespace
     * @param elementLocalName
     * @throws RSuiteException
     */
    public static void addElementToLmdIfNotAlready(ExecutionContext context, String lmdName, String namespace, String elementLocalName) throws RSuiteException {

        MetaDataService metaSvc = context.getMetaDataService();
        User user = context.getAuthorizationService().getSystemUser();
        LayeredMetadataDefinition def = metaSvc.getLayeredMetaDataDefinition(user, lmdName);
        if (def == null) {
            throw new RSuiteException("No metadata definition for " + lmdName);
        }

        ElementMatchingCriteria[] elemSet = def.getElementCriteria();

        if (elemSet == null || def.isAssociatedWithElementCriteria(namespace, elementLocalName, new ElementMatchingOptions()) == false) {
            List<ElementMatchingCriteria> updatedList = new ArrayList<ElementMatchingCriteria>();
            if (elemSet != null) {
                for (ElementMatchingCriteria emc : elemSet) {
                    updatedList.add(metaSvc.createElementMatchingCriteria(emc.getNamespaceUri(), emc.getLocalName()));
                }
            }
            updatedList.add(metaSvc.createElementMatchingCriteria(namespace, elementLocalName));
            metaSvc.setLayeredMetaDataDefinitionElementCriteria(user, lmdName, updatedList);
        }
    }
}
