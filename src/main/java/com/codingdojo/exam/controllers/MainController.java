package com.codingdojo.exam.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.codingdojo.exam.models.Task;
import com.codingdojo.exam.models.User;
import com.codingdojo.exam.repository.TaskRepository;
import com.codingdojo.exam.repository.UserRepository;
import com.codingdojo.exam.services.UserService;
import com.codingdojo.exam.validators.UserValidator;

@Controller
public class MainController {
	@Autowired
	private UserRepository urepo;
	@Autowired
	private UserService userv;
	@Autowired
	private UserValidator uvalid;
	@Autowired
	private TaskRepository trepo;
	
	public MainController(
			UserRepository urepo,
			UserService userv,
			UserValidator uvalid,
			TaskRepository trepo
			) {
		this.urepo = urepo;
		this.userv = userv;
		this.uvalid = uvalid;
		this.trepo = trepo;
	}
	
	@GetMapping("/dashboard")
	public String dashboard(HttpSession session, Model model) {
		if(session.getAttribute("user_id") == null) {
			return "redirect:/login";
		}
		Long id = (Long) session.getAttribute("user_id");
		model.addAttribute("user",urepo.findById(id).orElse(null));
		model.addAttribute("tasks", trepo.findAll());
		return "dashboard.jsp";
	}
	
	@GetMapping("/sorthigh")
	public String sortHigh(HttpSession session, Model model) {
		Long id = (Long) session.getAttribute("user_id");
		model.addAttribute("user",urepo.findById(id).orElse(null));
		List<Task> tasks = trepo.findAll();
		tasks.sort((t1,t2)->t1.getPriority()-t2.getPriority());
		model.addAttribute("tasks", tasks);
		return "dashboard.jsp";
	}
	
	@GetMapping("/sortlow")
	public String sortLow(HttpSession session, Model model) {
		Long id = (Long) session.getAttribute("user_id");
		model.addAttribute("user",urepo.findById(id).orElse(null));
		List<Task> tasks = trepo.findAll();
		tasks.sort((t1,t2)->t2.getPriority()-t1.getPriority());
		model.addAttribute("tasks", tasks);
		return "dashboard.jsp";
	}
	
	// New Task
	
	@GetMapping("/tasks/new")
	public String newTask(Model model) {
		model.addAttribute("task", new Task());
		model.addAttribute("users", urepo.findAll());
		return "newTask.jsp";
	}
	@PostMapping("/tasks/new")
	public String createTask(@Valid @ModelAttribute("task") Task task, BindingResult result, Model model, HttpSession session) {
		if(result.hasErrors()) {
			model.addAttribute("users", urepo.findAll());
			return "newTask.jsp";
		}
		if(task.getAssignee() != null) {
			if(task.getAssignee().getAssignedTasks().size() > 2) {
				model.addAttribute("users", urepo.findAll());
				model.addAttribute("error", task.getAssignee().getFirstName()+" is already assigned to 3 tasks -- Please choose another Assignee");
				return "newTask.jsp";
			}	
		}
		task.setCreator(urepo.findById((Long)session.getAttribute("user_id")).orElse(null));
		trepo.save(task);
		return "redirect:/dashboard";
	}
	
	// Display Task
	
	@GetMapping("/tasks/show/{task_id}")
	public String displayTask(@PathVariable("task_id") Long taskId, Model model) {
		model.addAttribute("task", trepo.findById(taskId).orElse(null));
		return "displayTask.jsp";
	}
	
	// Edit Task
	
	@RequestMapping("/tasks/edit/{task_id}")
	public String editTask(@PathVariable("task_id") Long taskId, Model model, HttpSession session) {
		Task task = trepo.findById(taskId).orElse(null);
		if(!session.getAttribute("user_id").equals(task.getCreator().getId())) {
			return "redirect:/dashboard";
		}
		model.addAttribute("task", task);
		model.addAttribute("users", urepo.findAll());
		return "editTask.jsp";
	}
	
	//update 
	@RequestMapping(value ="/tasks/edit/{task_id}", method=RequestMethod.PUT)
	public String doEditTask(@PathVariable("task_id") Long taskId, @Valid @ModelAttribute("task") Task task, BindingResult result, Model model) {
		if(result.hasErrors()) {
			task.setId(taskId);
			model.addAttribute("users", urepo.findAll());
			return "editTask.jsp";
		}
		if(task.getAssignee() != null) {
			if(task.getAssignee().getAssignedTasks().size() > 2) {
				task.setId(taskId);
				model.addAttribute("users", urepo.findAll());
				model.addAttribute("error", task.getAssignee().getFirstName()+" has 3 tasks already assigned.Please choose another Assignee");
				return "newTask.jsp";
			}	
		}
		Task t = trepo.findById(taskId).orElse(null);
		t.setName(task.getName());
		t.setAssignee(task.getAssignee());
		t.setPriority(task.getPriority());
		trepo.save(t);
		return "redirect:/dashboard";
	}
	
	// Destroy Task
	
	@RequestMapping(value ="/tasks/destroy/{task_id}", method=RequestMethod.DELETE)
	public String delete(@PathVariable("task_id") Long taskId) {
		trepo.deleteById(taskId);
		return "redirect:/dashboard";
	}
	
	// Login and Registration
	
	@GetMapping("/registration")
	public String registerUser(Model model) {
		model.addAttribute("user", new User());
		return "register.jsp";
	}
	@PostMapping("/registration")
	public String doRegisterUser(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session) {
		uvalid.validate(user, result);
		if(result.hasErrors()) {
			return "register.jsp";
		}
		userv.registerUser(user);
		session.setAttribute("user_id", user.getId());
		return "redirect:/dashboard";
	}
	@GetMapping("/login")
	public String login() {
		return "login.jsp";
	}
	@PostMapping("/login")
	public String doLogin(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session) {
		if(!userv.authenticateUser(email, password)) {
			model.addAttribute("error", "Incorrect email / password combination");
			return "login.jsp";	
		}
		User user = urepo.findByEmail(email);
		session.setAttribute("user_id", user.getId());
		return "redirect:/dashboard";
	}
	@GetMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
	
	

}
