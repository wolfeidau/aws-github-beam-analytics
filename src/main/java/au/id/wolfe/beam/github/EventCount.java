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
package au.id.wolfe.beam.github;

import au.id.wolfe.beam.github.data.GithubEvents;
import au.id.wolfe.beam.github.data.GithubEvents.GitHubEvent;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.schemas.NoSuchSchemaException;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.transforms.Convert;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.JsonToRow;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class EventCount {
    private static final Logger LOG = LoggerFactory.getLogger(EventCount.class);

    static void runEventCount(EventCountOptions options) throws NoSuchSchemaException {
        Pipeline p = Pipeline.create(options);

        Schema objectSchema = p.getSchemaRegistry().getSchema(GitHubEvent.class);

        PCollection<String> lines = p.apply(TextIO.read().from(options.getInputFile()));

        PCollection<GithubEvents.GitHubEvent> events = lines
                .apply(JsonToRow.withSchema(objectSchema)).apply(Convert.to(GithubEvents.GitHubEvent.class));

        PCollection<String> actors = events
                .apply(MapElements.into(TypeDescriptors.strings()).via(t -> t.getActor().getLogin()));

        PCollection<KV<String, Long>> userSummary = actors.apply(Count.perElement());

        userSummary
                .apply(MapElements.into(TypeDescriptors.strings()).via(t -> String.format("%s,%s", t.getKey(), t.getValue())))
                .apply(TextIO.write().to(options.getOutput()).withSuffix("csv"));

        p.run().waitUntilFinish();
    }

    public static void main(String[] args) throws Exception {
        EventCountOptions options =
                PipelineOptionsFactory.fromArgs(args).withValidation().as(EventCountOptions.class);


        runEventCount(options);
    }
}

