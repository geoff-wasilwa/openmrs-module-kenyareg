package org.openmrs.module.kenyareg.page.controller;

import org.apache.commons.lang.StringUtils;
import org.go2itech.oecui.data.RequestResult;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.session.Session;
import org.springframework.web.bind.annotation.RequestParam;

public class MergePageController {

	public String get(
			@RequestParam(value = "lpiUid", required = false) String lpiUid,
			@RequestParam(value = "mpiUid", required = false) String mpiUid,
			@RequestParam(value = "returnUrl", required = false) String returnUrl,
			PageModel model, UiUtils ui, Session session) {
		RequestResult lpiResult = session.getAttribute("lpiResult", RequestResult.class);
		RequestResult mpiResult = session.getAttribute("lpiResult", RequestResult.class);
		if ((lpiUid != null || mpiUid != null) && lpiResult == null && mpiResult == null) {
			if (StringUtils.isNotBlank(returnUrl)) {
				return "redirect:" + returnUrl;
			}
			return "redirect:" + ui.pageLink("kenyareg", "registryHome");
		}
		model.addAttribute("lpiUid", lpiUid);
		model.addAttribute("mpiUid", mpiUid);
		model.addAttribute("returnUrl", returnUrl);
		return null;
	}

}
