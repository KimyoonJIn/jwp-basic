package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.QuestionDao;
import next.model.Question;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static next.controller.UserSessionUtils.getUserFromSession;


public class UpdateQuestionFormController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(UpdateQuestionFormController.class);
    private QuestionDao questionDao = QuestionDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse response) throws Exception {
        // 로그인한 사용자만 질문이 가능하도록
        if (getUserFromSession(req.getSession()) == null) {
            return jspView("redirect:/users/loginForm");
        }

        long questionId = Long.parseLong(req.getParameter("questionId"));
        Question question = questionDao.findById(questionId);
        User user = UserSessionUtils.getUserFromSession(req.getSession());

        if (user.getUserId() != question.getWriter()) {
            throw new IllegalStateException("Invalid User");
        }

        return jspView("/qna/update.jsp").addObject("question", question);
    }

}
