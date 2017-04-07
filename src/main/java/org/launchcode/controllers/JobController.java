package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, int id) {

        // TODO #1 - get the Job with the given ID and pass it into the view
        Job job = jobData.findById(id);
        model.addAttribute("job", job);
        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors) {

        // TODO #6 - Validate the JobForm model, and if valid, create a
        // new Job and add it to the jobData data store. Then
        // redirect to the job detail view for the new Job.

        if(errors.hasErrors()){
            model.addAttribute("jobForm", jobForm);
            return "new-job";
        }

        String newName = jobForm.getName();
        int employerId = jobForm.getEmployerId();
        int locationId = jobForm.getLocationId();
        int positionTypeId = jobForm.getPositionTypeId();
        int coreCompetencyId = jobForm.getCoreCompetencyId();

        // Is there is a better way to initialize these placeholder objects? null?
        String defaultVal = "default";
        Employer newEmployer = new Employer(defaultVal);
        Location newLocation = new Location(defaultVal);
        PositionType newPositionType = new PositionType(defaultVal);
        CoreCompetency newCoreCompetency = new CoreCompetency(defaultVal);

        // Why can't I just declare & initialize object within the loop?
        for(Employer employer: jobForm.getEmployers()) {
            if (employer.getId() == jobForm.getEmployerId()) {
                newEmployer = employer;
                break;
            }
        }

        for(Location location: jobForm.getLocations()) {
            if (location.getId() == jobForm.getLocationId()) {
                newLocation = location;
                break;
            }
        }

        for(PositionType positionType: jobForm.getPositionTypes()) {
            if (positionType.getId() == jobForm.getPositionTypeId()) {
                newPositionType = positionType;
                break;
            }
        }

        for(CoreCompetency coreCompetency: jobForm.getCoreCompetencies()) {
            if (coreCompetency.getId() == jobForm.getCoreCompetencyId()) {
                newCoreCompetency = coreCompetency;
                break;
            }
        }

        Job newJob = new Job(newName, newEmployer, newLocation, newPositionType, newCoreCompetency);
        jobData.add(newJob);

        int newJobId = newJob.getId();
        return "redirect:?id=" + newJobId;
    }
}
