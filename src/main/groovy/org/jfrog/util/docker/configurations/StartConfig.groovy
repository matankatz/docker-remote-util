/*
 * Copyright (C) 2015 JFrog Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jfrog.util.docker.configurations

import com.google.gson.Gson

/**
 * Created by matank on 4/20/15.
 */
class StartConfig {
    Map<String, Object> HostConfig = [
            PortBindings: [:],
            Binds       : [],
            Links       : [],
            Privileged  : false
    ]

    StartConfig addLink(String containerName, String internalName) {
        this.HostConfig.Links.add(containerName + ':' + internalName)
        return this
    }

    StartConfig addPortBinding(int containerPort, String protocol, String hostIp, int hostPort) {

        String key = containerPort + '/' + protocol
        if (!this.HostConfig.PortBindings.containsKey(key)) {
            this.HostConfig.PortBindings.put(key, [])
        }

        this.HostConfig.PortBindings[key].add(
                [
                        HostIp  : hostIp,
                        HostPort: hostPort.toString()
                ]
        )

        return this
    }

    StartConfig addBinds(String hostPath, String containerPath) {
        this.HostConfig.Binds.add(hostPath + ":" + containerPath)
        return this
    }

    /**
     * Set container to run with porg.jfrog.qa.dockerges on docker server
     * @return
     */
    StartConfig withPrivileges() {
        this.HostConfig.Privileged = true
        return this
    }

    String toJson() {
        Gson gson = new Gson()
        return gson.toJson(this)
    }
}
