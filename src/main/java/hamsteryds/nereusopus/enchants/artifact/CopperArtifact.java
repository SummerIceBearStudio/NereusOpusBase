package hamsteryds.nereusopus.enchants.artifact;

import hamsteryds.nereusopus.enchants.internal.enchants.entries.ArtifactEnchantment;
import hamsteryds.nereusopus.utils.api.WorldUtils;
import org.bukkit.Particle;

import java.io.File;

public class CopperArtifact extends ArtifactEnchantment {
    public CopperArtifact(File file) {
        super(file);
    }

    @Override
    public Particle getParticle() {
        return WorldUtils.getParticle("WAX_ON");
    }
}
