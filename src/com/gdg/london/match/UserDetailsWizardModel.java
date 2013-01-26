/*
 * Copyright 2012 Roman Nurik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gdg.london.match;

import com.example.android.wizardpager.wizard.model.AbstractWizardModel;
import com.example.android.wizardpager.wizard.model.BranchPage;
import com.example.android.wizardpager.wizard.model.MultipleFixedChoicePage;
import com.example.android.wizardpager.wizard.model.PageList;
import com.example.android.wizardpager.wizard.model.SingleFixedChoicePage;

import android.content.Context;

public class UserDetailsWizardModel extends AbstractWizardModel {
    public UserDetailsWizardModel(Context context) {
        super(context);
    }

    @Override
	protected PageList onNewRootPageList() {
		return new PageList(
				new UserInfoPage(this, "Your info").setRequired(true),

				new BranchPage(this, "Participant type")
						.addBranch(
								"Designer",
								new SingleFixedChoicePage(this, "Fields")
										.setChoices("UX", "UI", "Interaction")
										.setRequired(true))
						.addBranch(
								"Developer",
								new BranchPage(this, "Speciality").addBranch(
										"Front End",
										new MultipleFixedChoicePage(this,
												"Platform").setChoices("iOS",
												"Android", "Web")).setValue(
										"No")
										.addBranch(
												"Back End",
												new MultipleFixedChoicePage(this,
														"Language").setChoices("Java",
														"Ruby", "PHP", "Node")).setValue(
												"No"))
						.addBranch(
								"Other",
								new SingleFixedChoicePage(this, "General skills")
										.setChoices("Ideas person", "PM")
										.setRequired(true))
						.setRequired(true)

		);
	}
}
