package org.ccframe.subsys.bike.service;

import org.springframework.stereotype.Service;
import org.ccframe.subsys.bike.domain.entity.BikeLockedStat;
import org.ccframe.subsys.bike.repository.BikeLockedStatRepository;
import org.ccframe.commons.base.BaseService;

@Service
public class BikeLockedStatService extends BaseService<BikeLockedStat,java.lang.Integer, BikeLockedStatRepository>{

}
