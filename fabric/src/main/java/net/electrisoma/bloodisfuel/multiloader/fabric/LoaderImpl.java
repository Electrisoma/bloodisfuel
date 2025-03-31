package net.electrisoma.bloodisfuel.multiloader.fabric;

import net.electrisoma.bloodisfuel.multiloader.Loader;


public class LoaderImpl {
    public static Loader getCurrent() {
        return Loader.FABRIC;
    }
}