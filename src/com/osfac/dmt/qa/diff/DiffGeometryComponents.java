package com.osfac.dmt.qa.diff;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.task.TaskMonitor;
import java.util.*;

public class DiffGeometryComponents {

  private FeatureCollection[] inputFC = new FeatureCollection[2];
  private TaskMonitor monitor;
  private DiffGeometryMatcher diffMatcher = new ExactGeometryMatcher();
  private boolean splitIntoComponents = true;
  
  final static String sMatchingfeatures = I18N.get("com.osfac.dmt.qa.diff.DiffGeometry.Matching-features");
  final static String sGeometries = I18N.get("com.osfac.dmt.qa.diff.DiffGeometryComponents.geometries");

  public DiffGeometryComponents(FeatureCollection fc0,
                                FeatureCollection fc1,
                                TaskMonitor monitor)
  {
    inputFC[0] = fc0;
    inputFC[1] = fc1;
    this.monitor = monitor;
  }

  public void setNormalize(boolean normalizeGeometry)
  {
    diffMatcher = new NormalizedExactGeometryMatcher();
  }

  public void setSplitIntoComponents(boolean splitIntoComponents)
  {
    this.splitIntoComponents = splitIntoComponents;
  }

  public void setMatcher(DiffGeometryMatcher diffMatcher)
  {
    this.diffMatcher = diffMatcher;
  }

  public FeatureCollection[] diff()
  {
    MatchCollection[] mc = {
      new MatchCollection(inputFC[0], splitIntoComponents),
      new MatchCollection(inputFC[1], splitIntoComponents) };
    compute(mc[0], mc[1]);

    return new FeatureCollection[] {
      mc[0].getUnmatchedFeatures(),
      mc[1].getUnmatchedFeatures() };

  }

  private void compute(MatchCollection mc0, MatchCollection mc1)
  {
    MatchIndex index = new MatchIndex(mc1);

    monitor.report(sMatchingfeatures);
    FeatureCollection[] diffFC = new FeatureCollection[2];
    matchFeatures(mc0, index);

    // compute feature matches based on own geometries
    mc0.computeFeatureMatches();
    mc1.computeFeatureMatches();
    // compute matches based on matched geometries
    mc0.propagateUnmatchedFeatures();
    mc1.propagateUnmatchedFeatures();
  }

  private void matchFeatures(MatchCollection matchColl, MatchIndex index)
  {
    int count = 1;
    int totalItems = matchColl.geometrySize();
    for (Iterator i = matchColl.geometryIterator(); i.hasNext(); ) {
      monitor.report(count++, totalItems, sGeometries);
      MatchGeometry matchGeom = (MatchGeometry) i.next();
      index.testMatch(matchGeom, diffMatcher);
    }
  }

}
