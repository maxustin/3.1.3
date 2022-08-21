package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.UserAuth;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.ArrayList;

@Controller
@RequestMapping(value = "/admin")
public class AdminsController {

	private UserService userService;
	private RoleService roleService;

	public AdminsController(UserService userService, RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
	}

	@GetMapping()
	public String printTable(Model model, Principal principal) {
		model.addAttribute("usertable", userService.getAllUsersFromDatabase());
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		model.addAttribute("newUser", new UserAuth());
		model.addAttribute("roles", roleService.getAllRolesFromDatabase());
		return "admin/user_table";
	}

	@PostMapping("/new")
	public String create(@ModelAttribute("newUser") UserAuth user, @RequestParam("listRoles") ArrayList<Long> roles){
		user.addRoles(roleService.findByIdRoles(roles));
		userService.addUserToDatabase(user);
		return "redirect:/admin";
	}


	@PatchMapping("/{id}/patch")
	public String update(@ModelAttribute("user") UserAuth user, @RequestParam("listRoles") ArrayList<Long> roles) {
		user.addRoles(roleService.findByIdRoles(roles));
		userService.updateUserInDatabase(user);
		return "redirect:/admin";
	}

	@DeleteMapping("/{id}/delete/user")
	public String delete(@PathVariable("id") int id) {
		userService.removeUserByIdFromDatabase(id);
		return "redirect:/admin";
	}
}