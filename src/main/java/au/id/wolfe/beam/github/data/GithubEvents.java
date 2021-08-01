/*
 * Copyright © 2021 Mark Wolfe (mark@wolfe.id.au)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.id.wolfe.beam.github.data;

import com.google.auto.value.AutoValue;
import org.apache.beam.sdk.annotations.Experimental;
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.annotations.SchemaCreate;
import org.apache.beam.sdk.schemas.annotations.SchemaFieldName;

import javax.annotation.Nullable;

@Experimental
public class GithubEvents {
    @AutoValue
    @DefaultSchema(AutoValueSchema.class)
    public abstract static class GitHubEvent {

        @SchemaFieldName("id")
        public @Nullable
        abstract String getId();

        public @Nullable
        abstract String getType();

        public @Nullable
        abstract GitHubActor getActor();


        @SchemaCreate
        public static GitHubEvent create(
                @Nullable String id,
                @Nullable String type,
                @Nullable GitHubActor actor) {
            return new AutoValue_GithubEvents_GitHubEvent(id, type, actor);
        }
    }
}
