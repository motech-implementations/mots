package org.motechproject.mots.utils;

import java.util.Comparator;
import org.motechproject.mots.domain.UnitProgress;

public class UnitProgressComparator implements Comparator<UnitProgress> {

  @Override
  public int compare(UnitProgress o1, UnitProgress o2) {
    return o1.getUnit().getListOrder().compareTo(o2.getUnit().getListOrder());
  }
}
