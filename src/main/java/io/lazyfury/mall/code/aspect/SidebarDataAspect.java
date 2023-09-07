package io.lazyfury.mall.code.aspect;


import io.lazyfury.mall.code.repository.ArticleTagRepository;
import io.lazyfury.mall.code.utils.AOPUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component

public class SidebarDataAspect {

    @Autowired
    ArticleTagRepository tagRepository;

    @Autowired
    AOPUtils aopUtils;

    @Before("@annotation(AddSidebarData)")
    public void addSidebarData(JoinPoint joinPoint) {
        System.out.println(Arrays.toString(joinPoint.getArgs()));
//        model.addObject("tags", tagRepository.findAll());
        Optional<ModelAndView> model = aopUtils.getParamByType(joinPoint, ModelAndView.class);
        model.ifPresent(m -> m.addObject("sidebar_tags", tagRepository.findAll()));
    }
}
