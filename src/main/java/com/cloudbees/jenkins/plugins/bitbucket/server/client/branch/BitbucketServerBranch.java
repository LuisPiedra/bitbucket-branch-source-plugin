/*
 * The MIT License
 *
 * Copyright (c) 2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.cloudbees.jenkins.plugins.bitbucket.server.client.branch;

import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketBranch;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;

public class BitbucketServerBranch implements BitbucketBranch {
    private static Logger LOGGER = Logger.getLogger(BitbucketServerBranch.class.getName());

    private String displayId;

    private String latestCommit;

    private Long timestamp;
    private Callable<Long> timestampClosure;

    public BitbucketServerBranch() {
    }

    public BitbucketServerBranch(String name, String headHash) {
        this.displayId = name;
        this.latestCommit = headHash;
    }

    @Override
    public String getRawNode() {
        return latestCommit;
    }

    @Override
    public String getName() {
        return displayId;
    }

    public long getTimestamp() {
        return timestamp();
    }

    @Override
    public long getDateMillis() {
        return timestamp();
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    public void setLatestCommit(String latestCommit) {
        this.latestCommit = latestCommit;
    }

    public void setName(String displayId) {
        this.displayId = displayId;
    }

    public void setRawNode(String latestCommit) {
        this.latestCommit = latestCommit;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Restricted(NoExternalUse.class)
    public void setTimestampClosure(Callable<Long> timestampClosure) {
        this.timestampClosure = timestampClosure;
    }

    private long timestamp() {
        if (timestamp == null) {
            if (timestampClosure == null) {
                timestamp = 0L;
            } else {
                try {
                    timestamp = timestampClosure.call();
                } catch (Exception e) {
                    LOGGER.log(Level.FINER, "Could not determine timestamp", e);
                }
            }
        }
        return timestamp;
    }

}
