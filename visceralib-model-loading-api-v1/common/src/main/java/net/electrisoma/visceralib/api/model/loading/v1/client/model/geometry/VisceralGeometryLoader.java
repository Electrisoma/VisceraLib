package net.electrisoma.visceralib.api.model.loading.v1.client.model.geometry;

import net.electrisoma.visceralib.api.model.loading.v1.client.model.VisceralUnbakedGeometry;

import com.google.gson.JsonObject;

public interface VisceralGeometryLoader {

	VisceralUnbakedGeometry read(JsonObject json);
}
