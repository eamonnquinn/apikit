/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.wsapi;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.processor.MessageProcessorChainBuilder;
import org.mule.api.processor.ProcessingStrategy;
import org.mule.construct.AbstractPipeline;
import org.mule.module.wsapi.api.QueryParamInterfaceDefinitionFilter;
import org.mule.module.wsapi.api.WebService;
import org.mule.module.wsapi.api.WebServiceInterface;

import java.util.List;

public abstract class AbstractWebService<T extends WebServiceInterface> extends AbstractPipeline
    implements WebService
{

    protected T webServiceInterface;

    public AbstractWebService(String name, T webServiceInterface, MuleContext muleContext)
    {
        super(name, muleContext);
        this.webServiceInterface = webServiceInterface;
    }

    @Override
    public T getInterface()
    {
        return webServiceInterface;
    }
    
    public void setInterface(T webServiveInterface)
    {
        this.webServiceInterface = webServiveInterface;
    }

    @Override
    protected void configureMessageProcessors(MessageProcessorChainBuilder builder) throws MuleException
    {
        builder.chain(getInterfaceRepresentationFilter());
        builder.chain(webServiceInterface.getOperationRouter());
    }

    @Override
    public void setProcessingStrategy(ProcessingStrategy processingStrategy)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMessageProcessors(List<MessageProcessor> messageProcessors)
    {
        throw new UnsupportedOperationException();
    }
    
    protected abstract QueryParamInterfaceDefinitionFilter getInterfaceRepresentationFilter();

}