package ru.pushpyshev.testtaskparqour.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.pushpyshev.testtaskparqour.dto.ShortenUrlDTO;
import ru.pushpyshev.testtaskparqour.service.ShorterService;
import ru.pushpyshev.testtaskparqour.service.IUrlRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Controller
public class UrlController {
	@Autowired
	private IUrlRepository urlStoreService;
	@Autowired
	private ShorterService shorterService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showForm(ShortenUrlDTO request) {
		return "shortener";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public void redirectToUrl(@PathVariable String id,
							  HttpServletRequest httpRequest,
							  HttpServletResponse resp) throws IOException {
		String requestUrl = httpRequest.getRequestURL().toString();
		String prefix = requestUrl.substring(0, requestUrl.indexOf(httpRequest.getRequestURI(),
				"http://".length()));
		final String url = urlStoreService.findUrlById(prefix + "/" + id);
		if (url != null) {
			resp.addHeader("Location", url);
			resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
		} else {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
 	}

	@RequestMapping(value = "/shrink", method = RequestMethod.POST)
	public ModelAndView shortenUrl(HttpServletRequest httpRequest,
								   @Valid ShortenUrlDTO shortenUrlDTO,
								   BindingResult bindingResult) {
		String url = shortenUrlDTO.getUrl();
		if (!isUrlValid(url)) {
			bindingResult.addError(new ObjectError("url", "Invalid url format: " + url));
		}

		ModelAndView modelAndView = new ModelAndView("shortener");
		if (!bindingResult.hasErrors()) {
			final String id = shorterService.encode(Math.abs(url.hashCode()));
			if (!isAlreadyShortened(url)) {
				String requestUrl = httpRequest.getRequestURL().toString();
				String prefix = requestUrl.substring(0, requestUrl.indexOf(httpRequest.getRequestURI(),
						"http://".length()));
				StringBuilder shortenUrl = new StringBuilder().append(prefix).append("/").append(id);
				urlStoreService.storeUrl(shortenUrl.toString(), url);
				modelAndView.addObject("shortenedUrl", shortenUrl);
			} else {
				modelAndView.addObject("shortenedUrl", url);
			}
		}
		return modelAndView;
	}

	private boolean isAlreadyShortened(String shortUrl) {
		return urlStoreService.findUrlById(shortUrl) != null;
	}

	private boolean isUrlValid(String url) {
		boolean valid = true;
		try {
			new URL(url);
		} catch (MalformedURLException e) {
			valid = false;
		}
		return valid;
	}
}