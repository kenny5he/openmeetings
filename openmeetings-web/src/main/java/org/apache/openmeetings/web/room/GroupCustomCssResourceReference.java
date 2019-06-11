/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") +  you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.openmeetings.web.room;

import static org.apache.openmeetings.util.OmFileHelper.getGroupCss;

import java.io.File;
import java.io.IOException;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.resource.FileSystemResource;
import org.apache.wicket.resource.FileSystemResourceReference;
import org.apache.wicket.util.string.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupCustomCssResourceReference extends FileSystemResourceReference {
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_NAME = "group-custom-css";
	private static final Logger log = LoggerFactory.getLogger(GroupCustomCssResourceReference.class);

	public GroupCustomCssResourceReference() {
		super(DEFAULT_NAME);
	}

	public GroupCustomCssResourceReference(String name) {
		super(name);
	}

	@Override
	public IResource getResource() {
		return new FileSystemResource() {
			private static final long serialVersionUID = 1L;
			private File file;

			@Override
			protected String getMimeType() throws IOException {
				return "text/css";
			}

			@Override
			protected ResourceResponse newResourceResponse(Attributes attr) {
				PageParameters params = attr.getParameters();
				StringValue idStr = params.get("id");
				Long id = null;
				try {
					id = idStr.toOptionalLong();
				} catch (NumberFormatException e) {
					//no-op expected
				}
				file = getGroupCss(id);
				if (file != null) {
					ResourceResponse rr = createResourceResponse(attr, file.toPath());
					rr.setFileName(file.getName());
					return rr;
				} else {
					log.debug("Custom CSS was not found was found");
					return null;
				}
			}
		};
	}
}
