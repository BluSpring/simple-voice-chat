package de.maxhenkel.voicechat.plugins.impl;

import de.maxhenkel.voicechat.api.VolumeCategory;

import javax.annotation.Nullable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class VolumeCategoryImpl implements VolumeCategory {

    public static final Pattern ID_REGEX = Pattern.compile("^[a-z_]{1,16}$");

    private final String id;
    private final String name;
    @Nullable
    private final String description;
    @Nullable
    private final int[][] icon;

    public VolumeCategoryImpl(String id, String name, @Nullable String description, @Nullable int[][] icon) {
        if (!ID_REGEX.matcher(id).matches()) {
            throw new IllegalArgumentException("Volume category ID can only contain a-z and _ with a maximum amount of 16 characters");
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public String getDescription() {
        return description;
    }

    @Nullable
    @Override
    public int[][] getIcon() {
        return icon;
    }

    public static VolumeCategoryImpl fromBytes(DataInputStream buf) throws IOException {
        String id = buf.readUTF();
        String name = buf.readUTF();
        String description = null;
        if (buf.readBoolean()) {
            description = buf.readUTF();
        }
        int[][] icon = null;
        if (buf.readBoolean()) {
            icon = new int[16][16];
            for (int x = 0; x < icon.length; x++) {
                for (int y = 0; y < icon.length; y++) {
                    icon[x][y] = buf.readInt();
                }
            }
        }
        return new VolumeCategoryImpl(id, name, description, icon);
    }

    public void toBytes(DataOutputStream buf) throws IOException {
        buf.writeUTF(id);
        buf.writeUTF(name);
        buf.writeBoolean(description != null);
        if (description != null) {
            buf.writeUTF(description);
        }
        buf.writeBoolean(icon != null);
        if (icon != null) {
            if (icon.length != 16) {
                throw new IllegalStateException("Icon is not 16x16");
            }
            for (int x = 0; x < icon.length; x++) {
                if (icon[x].length != 16) {
                    throw new IllegalStateException("Icon is not 16x16");
                }
                for (int y = 0; y < icon.length; y++) {
                    buf.writeInt(icon[x][y]);
                }
            }
        }
    }

    public static class BuilderImpl implements VolumeCategory.Builder {

        private String id;
        private String name;
        @Nullable
        private String description;
        @Nullable
        private int[][] icon;

        @Override
        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public Builder setDescription(@Nullable String description) {
            this.description = description;
            return this;
        }

        @Override
        public Builder setIcon(@Nullable int[][] icon) {
            this.icon = icon;
            return this;
        }

        @Override
        public VolumeCategory build() {
            if (id == null) {
                throw new IllegalStateException("id missing");
            }
            if (name == null) {
                throw new IllegalStateException("name missing");
            }
            return new VolumeCategoryImpl(id, name, description, icon);
        }
    }

}
