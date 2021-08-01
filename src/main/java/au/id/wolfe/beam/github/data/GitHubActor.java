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
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.annotations.SchemaCreate;
import org.apache.beam.sdk.schemas.annotations.SchemaFieldName;

import javax.annotation.Nullable;

@AutoValue
@DefaultSchema(AutoValueSchema.class)
public abstract class GitHubActor {
    @SchemaFieldName("id")
    public @Nullable
    abstract Long getID();

    public @Nullable
    abstract String getLogin();

    @SchemaFieldName("display_login")
    public @Nullable
    abstract String getDisplayLogin();

    @SchemaFieldName("url")
    public @Nullable
    abstract String getURL();

    @SchemaFieldName("avatar_url")
    public @Nullable
    abstract String getAvatarURL();

    @SchemaCreate
    public static GitHubActor create(
            @Nullable Long id,
            @Nullable String login,
            @Nullable String displayLogin,
            @Nullable String url,
            @Nullable String avatarURL) {
        return new AutoValue_GitHubActor(id, login, displayLogin, url, avatarURL);
    }
}