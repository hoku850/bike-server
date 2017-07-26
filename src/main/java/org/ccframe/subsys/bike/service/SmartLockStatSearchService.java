package org.ccframe.subsys.bike.service;

import java.util.List;

import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.subsys.bike.domain.entity.SmartLockStat;
import org.ccframe.subsys.bike.search.SmartLockStatSearchRepository;
import org.springframework.stereotype.Service;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

@Service
public class SmartLockStatSearchService extends BaseSearchService<SmartLockStat, Integer, SmartLockStatSearchRepository> {

	public List<SmartLockStat> findByLockSwitchStatCodeAndIfRepairIngAndLockLatBetweenAndLockLngBetween(String code, String string,
			double d, double e, double f, double g) {
		if(code!=null && string!=null) {
			return this.getRepository().findByLockSwitchStatCodeAndIfRepairIngAndLockLatBetweenAndLockLngBetween(code, string,
					d, e, f, g);
		}
		return null;
	}


}
