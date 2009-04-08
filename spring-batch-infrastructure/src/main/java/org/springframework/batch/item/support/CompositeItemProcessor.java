/*
 * Copyright 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.batch.item.support;

import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Composite {@link ItemProcessor} that passes the item through a sequence of
 * injected <code>ItemTransformer</code>s (return value of previous
 * transformation is the entry value of the next).<br/><br/>
 * 
 * Note the user is responsible for injecting a chain of {@link ItemProcessor}
 * s that conforms to declared input and output types.
 * 
 * @author Robert Kasanicky
 */
@SuppressWarnings("unchecked")
public class CompositeItemProcessor<I, O> implements ItemProcessor<I, O>, InitializingBean {

	private List<ItemProcessor> itemProcessors;

	public O process(I item) throws Exception {
		Object result = item;
	
		for(ItemProcessor transformer: itemProcessors){
			if(result == null){
				return null;
			}
			result = transformer.process(result);
		}
		return (O) result;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notEmpty(itemProcessors);
	}

	/**
	 * @param itemProcessors will be chained to produce a composite
	 * transformation.
	 */
	public void setItemProcessors(List<ItemProcessor> itemProcessors) {
		this.itemProcessors = itemProcessors;
	}

}
