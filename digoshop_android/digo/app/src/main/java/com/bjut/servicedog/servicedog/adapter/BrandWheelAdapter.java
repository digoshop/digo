/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;

import com.bjut.servicedog.servicedog.base.BaseWheelAdapter;
import com.bjut.servicedog.servicedog.po.Brand;

import java.util.List;


public class BrandWheelAdapter extends BaseWheelAdapter<Brand> {
	public BrandWheelAdapter(Context context, List<Brand> floors) {
		super(context,floors);
	}

	@Override
	protected CharSequence getItemText(int index) {
		Brand data = getItemData(index);
		if(data != null){
			String floor=data.getBrand_name();
			return floor;
		}
		return null;
	}
}
