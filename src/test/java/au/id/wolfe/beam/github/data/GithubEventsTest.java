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

import au.id.wolfe.beam.github.data.GithubEvents.GitHubEvent;
import org.apache.beam.sdk.schemas.NoSuchSchemaException;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.transforms.Convert;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.JsonToRow;
import org.apache.beam.sdk.values.PCollection;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GithubEventsTest {

    private static final GithubEvents.GitHubEvent AUTO_VALUE_EVENT = GithubEvents.GitHubEvent.create("17003395724", "PushEvent", GitHubActor.create(34305977L, "GenBrg", "GenBrg", "https://api.github.com/users/GenBrg", "https://avatars.githubusercontent.com/u/34305977?"));

    private static final String JSON = "{\"id\":\"17003395724\",\"type\":\"PushEvent\",\"actor\":{\"id\":34305977,\"login\":\"GenBrg\",\"display_login\":\"GenBrg\",\"gravatar_id\":\"\",\"url\":\"https://api.github.com/users/GenBrg\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/34305977?\"},\"repo\":{\"id\":372891397,\"name\":\"GenBrg/CampusSurvival\",\"url\":\"https://api.github.com/repos/GenBrg/CampusSurvival\"},\"payload\":{\"push_id\":7439786861,\"size\":1,\"distinct_size\":1,\"ref\":\"refs/heads/main\",\"head\":\"ef812ad37416e49c1c88e6e562e7d2994f5fda5b\",\"before\":\"573bf93a513787267170c9467dbe9c93625960d0\",\"commits\":[{\"sha\":\"ef812ad37416e49c1c88e6e562e7d2994f5fda5b\",\"author\":{\"name\":\"GenBrg\",\"email\":\"8e9c014b7db5a03854e8015213c4c7a86ba235ca@qq.com\"},\"message\":\"Finish build script backend\",\"distinct\":true,\"url\":\"https://api.github.com/repos/GenBrg/CampusSurvival/commits/ef812ad37416e49c1c88e6e562e7d2994f5fda5b\"}]},\"public\":true,\"created_at\":\"2021-07-01T15:00:00Z\"}";

    @Rule
    public transient TestPipeline pipeline = TestPipeline.create();

    @Test
    public void testParseCleanGithubEvent() throws NoSuchSchemaException {

        Schema objectSchema = pipeline.getSchemaRegistry().getSchema(GitHubEvent.class);

        PCollection<GithubEvents.GitHubEvent> events =
                pipeline
                        .apply(Create.of(JSON))
                        .apply(JsonToRow.withSchema(objectSchema))
                        .apply(Convert.to(GithubEvents.GitHubEvent.class));

        PAssert.that(events).containsInAnyOrder(AUTO_VALUE_EVENT);

        pipeline.run();
    }
}