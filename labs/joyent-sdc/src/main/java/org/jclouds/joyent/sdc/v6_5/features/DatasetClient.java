package org.jclouds.joyent.sdc.v6_5.features;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jclouds.concurrent.Timeout;
import org.jclouds.joyent.sdc.v6_5.domain.Dataset;

/**
 * Provides synchronous access to Datasets.
 * <p/>
 * 
 * @author Gerald Pereira
 * @see DatasetAsyncClient
 * @see <a href="http://apidocs.joyent.com/sdcapidoc/cloudapi">api doc</a>
 */
@Timeout(duration = 30, timeUnit = TimeUnit.SECONDS)
public interface DatasetClient {

   /**
    * Provides a list of datasets available in this datacenter.
    * 
    * @return
    */
   Set<Dataset> list();

   /**
    * Gets an individual dataset by id.
    * 
    * @param id
    *           the id of the dataset
    * @return
    */
   Dataset get(String id);
}
