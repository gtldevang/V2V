package interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class UserInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HttpSession session = request.getSession();
		Map<String, Object> model = (Map<String, Object>) modelAndView
				.getModel().get("model");
		User user = (User) session.getAttribute("user");
		if (user != null) {
			if (model == null) {
				model = new HashMap<String, Object>();
				model.put("user", user);
				modelAndView.addObject("model", model);
			} else {
				model.put("user", user);
			}
		}
		super.postHandle(request, response, handler, modelAndView);
	}
}
