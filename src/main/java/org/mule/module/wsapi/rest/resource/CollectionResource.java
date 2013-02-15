
package org.mule.module.wsapi.rest.resource;

import org.mule.module.wsapi.rest.action.ActionType;

import java.util.EnumSet;
import java.util.Set;

public class CollectionResource extends AbstractHierarchicalRestResource
{
    public CollectionResource(String name)
    {
        super(name);
    }

    @Override
    protected Set<ActionType> getSupportedActionTypes()
    {
        EnumSet<ActionType> set = EnumSet.copyOf(super.getSupportedActionTypes());
        set.add(ActionType.CREATE);
        return set;
    }

}
