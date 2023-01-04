package hamsteryds.nereusopus.enchants.artifact;

import hamsteryds.nereusopus.enchants.internal.enchants.entries.ArtifactEnchantment;
import org.bukkit.Particle;

import java.io.File;

public class TearArtifact extends ArtifactEnchantment {
    public TearArtifact(File file) {
        super(file);
    }

    @Override
    public Particle getParticle() {
        return Particle.DRIPPING_OBSIDIAN_TEAR;
    }
}
