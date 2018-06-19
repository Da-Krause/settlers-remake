/*******************************************************************************
 * Copyright (c) 2015
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package jsettlers.mapcreator.data;

import java.util.Set;

import jsettlers.common.buildings.EBuildingType;
import jsettlers.common.landscape.ELandscapeType;
import jsettlers.common.player.ECivilisation;
import jsettlers.mapcreator.data.objects.ProtectContainer;

public class ProtectLandscapeConstraint extends ProtectContainer implements LandscapeConstraint {

	private final EBuildingType buildingType;
	private final ECivilisation civilisation;

	public ProtectLandscapeConstraint(EBuildingType buildingType, ECivilisation civilisation) {
		this.buildingType = buildingType;
		this.civilisation = civilisation;
	}

	public Set<ELandscapeType> getAllowedLandscapes() {
		return buildingType.getGroundTypes(civilisation);
	}

	@Override
	public boolean needsFlattenedGround() {
		return buildingType.needsFlattenedGround(civilisation);
	}

}
