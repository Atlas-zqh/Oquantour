package oquantour.service;

import oquantour.exception.BackTestErrorException;
import oquantour.po.util.BackTestBestInfo;
import oquantour.po.util.BackTestInfo;
import oquantour.po.util.BackTestResult;

import java.util.List;

/**
 * Created by keenan on 07/05/2017.
 */
public interface BackTestService {
    /**
     * 获得回测结果
     *
     * @param backTestInfo 回测信息
     * @return 回测结果
     */
    BackTestResult getBackTestResult(BackTestInfo backTestInfo) throws BackTestErrorException;
}
