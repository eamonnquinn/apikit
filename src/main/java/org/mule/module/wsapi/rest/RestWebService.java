/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.wsapi.rest;

import org.mule.api.MessagingException;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.processor.MessageProcessor;
import org.mule.module.wsapi.AbstractWebService;
import org.mule.module.wsapi.rest.action.BaseResourceRetrieveAction;
import org.mule.module.wsapi.rest.action.RestAction;
import org.mule.module.wsapi.rest.resource.BaseResource;
import org.mule.module.wsapi.rest.resource.RestResource;
import org.mule.module.wsapi.rest.resource.StaticResourceCollection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestWebService extends AbstractWebService<RestWebServiceInterface>
{
    private boolean enableSwagger;

    public RestWebService(String name,
                          RestWebServiceInterface webServiceInterface,
                          boolean enableSwagger,
                          MuleContext muleContext)
    {
        super(name, webServiceInterface, muleContext);
        this.enableSwagger = enableSwagger;
    }

    @Override
    public String getConstructType()
    {
        return "REST-WEB-SERVICE";
    }

    public boolean isEnableSwagger()
    {
        return enableSwagger;
    }

    public void setEnableSwagger(boolean enableSwagger)
    {
        this.enableSwagger = enableSwagger;
    }

    @Override
    protected MessageProcessor getRequestRouter()
    {
        final BaseResource handler = new BaseResource();
        List<RestResource> resources = new ArrayList<RestResource>();
        resources.addAll((List<RestResource>) webServiceInterface.getRoutes());
        if (enableSwagger)
        {
            resources.add(new StaticResourceCollection("_swagger", "/org/mule/module/wsapi/rest/swagger"));
            handler.setActions(Collections.<RestAction> singletonList(new BaseResourceRetrieveAction(this)));
        }
        handler.setResources(resources);

        return new MessageProcessor()
        {
            @Override
            public MuleEvent process(MuleEvent event) throws MessagingException
            {
                try
                {
                    return handler.handle(new DefaultRestRequest(event, getInterface()));
                }
                catch (RestException e)
                {
                    throw new MessagingException(event, e);
                }
            }
        };

    }

}
