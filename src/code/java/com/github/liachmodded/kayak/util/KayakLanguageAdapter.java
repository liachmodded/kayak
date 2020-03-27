package com.github.liachmodded.kayak.util;

import net.fabricmc.loader.api.LanguageAdapter;
import net.fabricmc.loader.api.LanguageAdapterException;
import net.fabricmc.loader.api.ModContainer;

public final class KayakLanguageAdapter implements LanguageAdapter {

  @Override
  public <T> T create(ModContainer mod, String definition, Class<T> type) throws LanguageAdapterException {
    int index = definition.indexOf('#');
    return null;
  }
}
