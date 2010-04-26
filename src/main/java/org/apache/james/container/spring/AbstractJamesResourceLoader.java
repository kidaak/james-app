/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
package org.apache.james.container.spring;

import java.io.File;

import org.apache.james.services.FileSystem;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * Abstract base class which load JAMES files based on the prefix. This can be used in different 
 * {@link ApplicationContext} implementations
 * 
 *
 */
public abstract class AbstractJamesResourceLoader extends DefaultResourceLoader implements JamesResourceLoader{

    
    
    /**
     * Return the {@link Resource} for the given url. If the resource can not be found null get returned
     * 
     * @see org.springframework.core.io.ResourceLoader#getResource(java.lang.String)
     */
    public Resource getResource(String fileURL) {
        Resource r = null;
        if (fileURL.startsWith(FileSystem.CLASSPATH_PROTOCOL)) {
            String resourceName = fileURL.substring(FileSystem.CLASSPATH_PROTOCOL.length());
            r = new ClassPathResource(resourceName);
        } else if (fileURL.startsWith(FileSystem.FILE_PROTOCOL)) {
            File file = null;
            if (fileURL.startsWith(FileSystem.FILE_PROTOCOL_AND_CONF)) {
                file = new File(getConfDirectory() + "/" + fileURL.substring(FileSystem.FILE_PROTOCOL_AND_CONF.length()));
            } else if (fileURL.startsWith(FileSystem.FILE_PROTOCOL_AND_VAR)) {
                file = new File(getVarDirectory() + "/" + fileURL.substring(FileSystem.FILE_PROTOCOL_AND_VAR.length()));
            } else if (fileURL.startsWith(FileSystem.FILE_PROTOCOL_ABSOLUTE)) {
                file = new File(getAbsoluteDirectory() + fileURL.substring(FileSystem.FILE_PROTOCOL_ABSOLUTE.length()));
            } else {
                // move to the root folder of the spring deployment
                file = new File(getRootDirectory() + "/" + fileURL.substring(FileSystem.FILE_PROTOCOL.length()));
            }
            r = new FileSystemResource(file);
        } else {
            return null;
        }
        return r;
    }
    
}

