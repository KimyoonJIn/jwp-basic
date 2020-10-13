package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static next.controller.UserSessionUtils.getUserFromSession;

public class CreateQuestionController  extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(CreateQuestionController.class);

    private QuestionDao questionDao = new QuestionDao();

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse response) throws Exception {
        // 로그인한 사용자만 질문이 가능하도록
        if (getUserFromSession(req.getSession()) == null) {
            return jspView("redirect:/users/loginForm");
        }
        User user = getUserFromSession(req.getSession());

        // 질문할 때 글쓴이를 입력하지 않고 로그인한 사용자 정보를 가져와 글쓴이 이름으로 사용하도록 구현
        String writer = req.getParameter("writer");
        if (writer.equals("")){
            writer = user.getUserId();
        }

        Question question = new Question(writer, req.getParameter("title"),
                req.getParameter("contents"));
        Question savedQuestion = questionDao.insert(question);
        log.debug("question : {}", savedQuestion);

        return jspView("redirect:/");
    }
}
