package com.github.xiavic.essentials.Utils.warp;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface IPrivateWarpManager extends IWarpManager<PrivateWarp> {

    @NotNull Optional<PrivateWarp> getWarp(UUID owner, String name);

}
