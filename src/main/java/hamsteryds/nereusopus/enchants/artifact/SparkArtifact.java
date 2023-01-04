package hamsteryds.nereusopus.enchants.artifact;

import hamsteryds.nereusopus.enchants.internal.enchants.entries.ArtifactEnchantment;
import hamsteryds.nereusopus.utils.api.WorldUtils;
import org.bukkit.Particle;

import java.io.File;

public class SparkArtifact extends ArtifactEnchantment {
    public SparkArtifact(File file) {
        super(file);
    }

    @Override
    public Particle getParticle() {
        return WorldUtils.getParticle("ELECTRIC_SPARK");
    }
}
