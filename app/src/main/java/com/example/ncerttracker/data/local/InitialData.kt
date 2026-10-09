package com.example.ncerttracker.data.local

import com.example.ncerttracker.data.model.ChapterEntity

object InitialData {
    fun getChapters(): List<ChapterEntity> {
        val list = mutableListOf<ChapterEntity>()

        // ==========================================
        // JEE PHYSICS - CLASS 11
        // ==========================================
        list.add(
            ChapterEntity(
                id = "P11_01",
                subject = "PHYSICS",
                grade = 11,
                chapterNumber = 1,
                title = "Units and Measurements",
                subBranch = "Measurements",
                subtopics = "SI Units & Base Quantities|Dimensions of Physical Quantities|Dimensional Analysis & Applications|Errors in Measurement & Propagation|Significant Figures & Vernier/Screw Gauge"
            )
        )
        list.add(
            ChapterEntity(
                id = "P11_02",
                subject = "PHYSICS",
                grade = 11,
                chapterNumber = 2,
                title = "Kinematics in 1D & 2D",
                subBranch = "Kinematics",
                subtopics = "Position-Time & Velocity-Time Graphs|Uniformly Accelerated Motion Equations|Relative Motion in 1D & River-Boat Problems|Vectors, Components & Scalar/Vector Product|Projectile Motion on Horizontal & Incline"
            )
        )
        list.add(
            ChapterEntity(
                id = "P11_03",
                subject = "PHYSICS",
                grade = 11,
                chapterNumber = 3,
                title = "Laws of Motion",
                subBranch = "Mechanics",
                subtopics = "Newton's Laws & Inertial Frames|Free Body Diagrams & Pulley Systems|Friction: Static, Kinetic & Rolling|Banking of Curved Roads|Circular Motion Dynamics & Centripetal Force"
            )
        )
        list.add(
            ChapterEntity(
                id = "P11_04",
                subject = "PHYSICS",
                grade = 11,
                chapterNumber = 4,
                title = "Work, Energy and Power",
                subBranch = "Mechanics",
                subtopics = "Work Done by Constant & Variable Force|Kinetic Energy & Work-Energy Theorem|Potential Energy of Spring & Conservative Force|Conservation of Mechanical Energy & Power|Elastic & Inelastic Collisions in 1D & 2D"
            )
        )
        list.add(
            ChapterEntity(
                id = "P11_05",
                subject = "PHYSICS",
                grade = 11,
                chapterNumber = 5,
                title = "Rotational Motion & Centre of Mass",
                subBranch = "Mechanics",
                subtopics = "Centre of Mass for Particles & Continuous Bodies|Torque & Angular Momentum Conservation|Moment of Inertia, Parallel & Perpendicular Axes|Kinematics & Dynamics of Rotational Motion|Rolling Motion without Slipping on Incline"
            )
        )
        list.add(
            ChapterEntity(
                id = "P11_06",
                subject = "PHYSICS",
                grade = 11,
                chapterNumber = 6,
                title = "Gravitation",
                subBranch = "Gravitation",
                subtopics = "Kepler's Laws of Planetary Motion|Universal Law of Gravitation & Acceleration due to Gravity 'g'|Variation of 'g' with Altitude, Depth & Rotation|Gravitational Potential Energy & Escape Velocity|Orbital Velocity, Satellites & Weightlessness"
            )
        )
        list.add(
            ChapterEntity(
                id = "P11_07",
                subject = "PHYSICS",
                grade = 11,
                chapterNumber = 7,
                title = "Mechanical Properties of Solids",
                subBranch = "Properties of Matter",
                subtopics = "Stress-Strain Relationship & Hooke's Law|Young's, Bulk & Shear Modulus|Elastic Potential Energy in a Stretched Wire|Poisson's Ratio & Thermal Stress"
            )
        )
        list.add(
            ChapterEntity(
                id = "P11_08",
                subject = "PHYSICS",
                grade = 11,
                chapterNumber = 8,
                title = "Mechanical Properties of Fluids",
                subBranch = "Properties of Matter",
                subtopics = "Pressure, Pascal's Principle & Archimedes Principle|Streamline Flow, Continuity Equation & Bernoulli's Theorem|Torricelli's Law & Venturimeter|Viscosity, Stokes' Law & Terminal Velocity|Surface Tension, Surface Energy, Contact Angle & Capillarity"
            )
        )
        list.add(
            ChapterEntity(
                id = "P11_09",
                subject = "PHYSICS",
                grade = 11,
                chapterNumber = 9,
                title = "Thermal Properties of Matter",
                subBranch = "Heat & Thermodynamics",
                subtopics = "Heat, Temperature Scales & Thermal Expansion|Specific Heat Capacity & Calorimetry|Latent Heat & Phase Transitions|Heat Transfer: Conduction, Convection & Radiation|Newton's Law of Cooling & Stefan-Boltzmann Law"
            )
        )
        list.add(
            ChapterEntity(
                id = "P11_10",
                subject = "PHYSICS",
                grade = 11,
                chapterNumber = 10,
                title = "Thermodynamics",
                subBranch = "Heat & Thermodynamics",
                subtopics = "Zeroth & First Law of Thermodynamics|Isothermal, Adiabatic, Isobaric & Isochoric Processes|Work Done in Thermodynamic Processes|Carnot Engine, Efficiency & Refrigerator Coefficient|Second Law of Thermodynamics & Entropy Concept"
            )
        )
        list.add(
            ChapterEntity(
                id = "P11_11",
                subject = "PHYSICS",
                grade = 11,
                chapterNumber = 11,
                title = "Kinetic Theory of Gases",
                subBranch = "Heat & Thermodynamics",
                subtopics = "Equation of State for Ideal Gas|Assumptions of Kinetic Theory & Pressure of Gas|RMS, Average & Most Probable Speeds|Degrees of Freedom & Law of Equipartition of Energy|Specific Heat Capacities of Gases (Cp, Cv) & Mean Free Path"
            )
        )
        list.add(
            ChapterEntity(
                id = "P11_12",
                subject = "PHYSICS",
                grade = 11,
                chapterNumber = 12,
                title = "Oscillations (SHM)",
                subBranch = "Oscillations & Waves",
                subtopics = "Simple Harmonic Motion Displacement, Velocity & Acceleration|Kinetic, Potential & Total Energy in SHM|Spring-Block System & Combinations of Springs|Simple Pendulum & Angular SHM|Damped Oscillations, Forced Oscillations & Resonance"
            )
        )
        list.add(
            ChapterEntity(
                id = "P11_13",
                subject = "PHYSICS",
                grade = 11,
                chapterNumber = 13,
                title = "Waves and Acoustics",
                subBranch = "Oscillations & Waves",
                subtopics = "Transverse & Longitudinal Wave Equations|Speed of Sound & Laplace's Correction|Superposition Principle & Interference of Waves|Standing Waves in Stretched Strings & Organ Pipes|Beats Phenomenon & Doppler Effect in Sound"
            )
        )

        // ==========================================
        // JEE PHYSICS - CLASS 12
        // ==========================================
        list.add(
            ChapterEntity(
                id = "P12_01",
                subject = "PHYSICS",
                grade = 12,
                chapterNumber = 1,
                title = "Electrostatics & Electric Fields",
                subBranch = "Electrostatics",
                subtopics = "Coulomb's Law in Vector Form & Superposition|Electric Field due to Point & Continuous Charges|Electric Dipole, Field on Axial/Equatorial & Torque in External Field|Electric Flux & Gauss's Law Applications (Wire, Sheet, Sphere)"
            )
        )
        list.add(
            ChapterEntity(
                id = "P12_02",
                subject = "PHYSICS",
                grade = 12,
                chapterNumber = 2,
                title = "Electrostatic Potential & Capacitance",
                subBranch = "Electrostatics",
                subtopics = "Electric Potential, Potential Gradient & Equipotential Surfaces|Potential Energy of Charge System & Dipole|Parallel Plate Capacitor & Dielectric Polarization|Capacitance Combinations: Series, Parallel & Mixed|Energy Stored in Capacitor & Common Potential"
            )
        )
        list.add(
            ChapterEntity(
                id = "P12_03",
                subject = "PHYSICS",
                grade = 12,
                chapterNumber = 3,
                title = "Current Electricity",
                subBranch = "Current Electricity",
                subtopics = "Electric Current, Drift Velocity & Ohm's Law|Resistivity, Temperature Dependence & Color Code|EMF, Internal Resistance & Grouping of Cells|Kirchhoff's Voltage & Current Laws (KVL/KCL)|Wheatstone Bridge, Meter Bridge & Potentiometer Principle"
            )
        )
        list.add(
            ChapterEntity(
                id = "P12_04",
                subject = "PHYSICS",
                grade = 12,
                chapterNumber = 4,
                title = "Magnetic Effects of Current",
                subBranch = "Magnetism",
                subtopics = "Biot-Savart Law & Field due to Straight Wire & Circular Loop|Ampere's Circuital Law & Solenoid/Toroid Field|Lorentz Force & Helical Motion in Magnetic Field|Force on Current-Carrying Conductor & Parallel Wires|Torque on Magnetic Loop & Moving Coil Galvanometer (Ammeter/Voltmeter conversion)"
            )
        )
        list.add(
            ChapterEntity(
                id = "P12_05",
                subject = "PHYSICS",
                grade = 12,
                chapterNumber = 5,
                title = "Magnetism and Matter",
                subBranch = "Magnetism",
                subtopics = "Bar Magnet as Equivalent Solenoid & Magnetic Field Lines|Magnetic Dipole Moment & Torque in Uniform Field|Earth's Magnetic Field: Declination, Dip & Horizontal Component|Dia, Para & Ferromagnetic Substances|Curie's Law & Hysteresis Loop"
            )
        )
        list.add(
            ChapterEntity(
                id = "P12_06",
                subject = "PHYSICS",
                grade = 12,
                chapterNumber = 6,
                title = "Electromagnetic Induction",
                subBranch = "Electromagnetism",
                subtopics = "Magnetic Flux & Faraday's Laws of Induction|Lenz's Law & Conservation of Energy|Motional EMF in Rotating & Translating Conductors|Self & Mutual Inductance (Inductors in Series/Parallel)|Eddy Currents & AC Generator Principle"
            )
        )
        list.add(
            ChapterEntity(
                id = "P12_07",
                subject = "PHYSICS",
                grade = 12,
                chapterNumber = 7,
                title = "Alternating Current (AC)",
                subBranch = "Electromagnetism",
                subtopics = "Peak, RMS & Average Values of AC Voltage and Current|Phasor Diagrams for R, L and C Circuits|Series LCR Circuit, Impedance & Resonance Curve|Quality Factor (Q-factor) & Power Factor in AC Circuits|Wattless Current & Transformer (Step-up/Step-down)"
            )
        )
        list.add(
            ChapterEntity(
                id = "P12_08",
                subject = "PHYSICS",
                grade = 12,
                chapterNumber = 8,
                title = "Electromagnetic Waves",
                subBranch = "Electromagnetism",
                subtopics = "Displacement Current & Modified Ampere's Law|Maxwell's Equations & Transverse Nature of EM Waves|Energy Density & Poynting Vector in EM Waves|Electromagnetic Spectrum (Gamma to Radio Waves) & Characteristics"
            )
        )
        list.add(
            ChapterEntity(
                id = "P12_09",
                subject = "PHYSICS",
                grade = 12,
                chapterNumber = 9,
                title = "Ray Optics and Optical Instruments",
                subBranch = "Optics",
                subtopics = "Reflection from Spherical Mirrors & Mirror Formula|Refraction, Snell's Law & Total Internal Reflection (Optical Fiber)|Refraction at Spherical Surfaces & Thin Lens Formula|Lens Maker Formula & Combination of Thin Lenses|Prism Dispersion, Angle of Minimum Deviation|Compound Microscope & Astronomical Telescope"
            )
        )
        list.add(
            ChapterEntity(
                id = "P12_10",
                subject = "PHYSICS",
                grade = 12,
                chapterNumber = 10,
                title = "Wave Optics",
                subBranch = "Optics",
                subtopics = "Huygens' Principle & Wavefront Construction|Reflection & Refraction Proof by Wave Theory|Interference of Light & Young's Double Slit Experiment (YDSE)|Fringe Width, Shift due to Thin Sheet & Intensity Distribution|Diffraction at a Single Slit & Central Maxima Width|Polarisation of Light, Brewster's Law & Malus's Law"
            )
        )
        list.add(
            ChapterEntity(
                id = "P12_11",
                subject = "PHYSICS",
                grade = 12,
                chapterNumber = 11,
                title = "Dual Nature of Radiation & Matter",
                subBranch = "Modern Physics",
                subtopics = "Photoelectric Effect, Threshold Frequency & Stopping Potential|Einstein's Photoelectric Equation & Experimental Verification|Photon Concept, Momentum & Energy Relations|de Broglie Hypothesis & Wavelength of Electrons/Particles|Davisson and Germer Experiment Concept"
            )
        )
        list.add(
            ChapterEntity(
                id = "P12_12",
                subject = "PHYSICS",
                grade = 12,
                chapterNumber = 12,
                title = "Atoms",
                subBranch = "Modern Physics",
                subtopics = "Alpha-Particle Scattering Experiment & Rutherford Model|Bohr Model Postulates for Hydrogen-like Atoms|Radius, Velocity & Energy of Electron in nth Orbit|Hydrogen Emission Spectrum (Lyman, Balmer, Paschen)|de Broglie's Explanation of Bohr's Quantization Rule"
            )
        )
        list.add(
            ChapterEntity(
                id = "P12_13",
                subject = "PHYSICS",
                grade = 12,
                chapterNumber = 13,
                title = "Nuclei",
                subBranch = "Modern Physics",
                subtopics = "Nuclear Composition, Size & Density|Mass Defect & Binding Energy per Nucleon Curve|Nuclear Forces Characteristics (Strong, Short-Range)|Radioactive Decay Law, Half-Life & Mean Life|Alpha, Beta & Gamma Decay Processes|Nuclear Fission, Chain Reaction & Nuclear Fusion"
            )
        )
        list.add(
            ChapterEntity(
                id = "P12_14",
                subject = "PHYSICS",
                grade = 12,
                chapterNumber = 14,
                title = "Semiconductor Electronics",
                subBranch = "Electronics",
                subtopics = "Energy Bands in Solids (Conductors, Insulators, Semiconductors)|Intrinsic & Extrinsic Semiconductors (p-type, n-type)|p-n Junction Formation, Barrier Potential & Depletion Layer|Forward & Reverse Bias V-I Characteristics|p-n Junction as Half-Wave & Full-Wave Rectifier|Zener Diode as Voltage Regulator|Logic Gates: AND, OR, NOT, NAND, NOR Truth Tables"
            )
        )
        list.add(
            ChapterEntity(
                id = "P12_15",
                subject = "PHYSICS",
                grade = 12,
                chapterNumber = 15,
                title = "Experimental Skills in Physics",
                subBranch = "Practical Physics",
                subtopics = "Vernier Calipers & Screw Gauge Measurements|Simple Pendulum (Determination of g)|Young's Modulus of Wire by Searle's Apparatus|Surface Tension by Capillary Rise Method|Metre Bridge & Potentiometer Unknown Resistance|Focal Length of Concave Mirror & Convex Lens"
            )
        )

        // ==========================================
        // JEE CHEMISTRY - CLASS 11
        // ==========================================
        list.add(
            ChapterEntity(
                id = "C11_01",
                subject = "CHEMISTRY",
                grade = 11,
                chapterNumber = 1,
                title = "Some Basic Concepts of Chemistry",
                subBranch = "Physical Chemistry",
                subtopics = "Mole Concept, Molar Mass & Avogadro's Number|Stoichiometry & Limiting Reagent Calculations|Concentration Terms: Molarity, Molality, Mole Fraction & % w/w|Empirical & Molecular Formula Determination|Laws of Chemical Combinations & Significant Figures"
            )
        )
        list.add(
            ChapterEntity(
                id = "C11_02",
                subject = "CHEMISTRY",
                grade = 11,
                chapterNumber = 2,
                title = "Structure of Atom",
                subBranch = "Physical Chemistry",
                subtopics = "Bohr's Atomic Model & Hydrogen Line Spectrum|Dual Nature of Matter: de Broglie Wavelength|Heisenberg's Uncertainty Principle|Quantum Numbers (n, l, m, s) & Orbital Shapes|Aufbau Principle, Pauli Exclusion & Hund's Rule of Maximum Multiplicity"
            )
        )
        list.add(
            ChapterEntity(
                id = "C11_03",
                subject = "CHEMISTRY",
                grade = 11,
                chapterNumber = 3,
                title = "Classification of Elements & Periodicity",
                subBranch = "Inorganic Chemistry",
                subtopics = "Modern Periodic Law & Periodic Table Layout (s, p, d, f blocks)|Periodic Trends in Atomic & Ionic Radii|Ionization Enthalpy & Electron Gain Enthalpy Trends|Electronegativity Scales (Pauling) & Anomalous Trends|Valency & Chemical Reactivity Periodicity"
            )
        )
        list.add(
            ChapterEntity(
                id = "C11_04",
                subject = "CHEMISTRY",
                grade = 11,
                chapterNumber = 4,
                title = "Chemical Bonding & Molecular Structure",
                subBranch = "Inorganic Chemistry",
                subtopics = "Ionic Bond, Lattice Enthalpy & Born-Haber Cycle|Covalent Bond, Octet Rule & Fajan's Rules|VSEPR Theory & Prediction of Molecular Geometries|Hybridization (sp, sp2, sp3, sp3d, sp3d2) & Resonance|Molecular Orbital Theory: Homonuclear Diatomics & Bond Order|Dipole Moment, Polar Bonds & Hydrogen Bonding (Inter/Intramolecular)"
            )
        )
        list.add(
            ChapterEntity(
                id = "C11_05",
                subject = "CHEMISTRY",
                grade = 11,
                chapterNumber = 5,
                title = "Chemical Thermodynamics",
                subBranch = "Physical Chemistry",
                subtopics = "State Functions, Internal Energy (U) & First Law of Thermodynamics|Work, Heat & Enthalpy (H = U + PV)|Hess's Law of Constant Heat Summation & Bond Enthalpies|Entropy (S) & Second Law of Thermodynamics|Gibbs Free Energy (G), Spontaneity Criteria & Equilibrium Constant Relation"
            )
        )
        list.add(
            ChapterEntity(
                id = "C11_06",
                subject = "CHEMISTRY",
                grade = 11,
                chapterNumber = 6,
                title = "Equilibrium (Chemical & Ionic)",
                subBranch = "Physical Chemistry",
                subtopics = "Law of Chemical Equilibrium, Kc & Kp Relations|Le Chatelier's Principle (Effect of Temp, Pressure, Concentration)|Arrhenius, Bronsted-Lowry & Lewis Acid-Base Concepts|Ionic Product of Water (Kw), pH Scale & Ostwald Dilution Law|Hydrolysis of Salts & Buffer Solutions (Henderson Equation)|Solubility Product (Ksp) & Common Ion Effect in Precipitation"
            )
        )
        list.add(
            ChapterEntity(
                id = "C11_07",
                subject = "CHEMISTRY",
                grade = 11,
                chapterNumber = 7,
                title = "Redox Reactions",
                subBranch = "Physical Chemistry",
                subtopics = "Oxidation Number Concept & Rules|Balancing Redox Equations: Oxidation Number Method|Balancing Redox Equations: Ion-Electron Method|Electrochemical Series & Oxidizing/Reducing Power"
            )
        )
        list.add(
            ChapterEntity(
                id = "C11_08",
                subject = "CHEMISTRY",
                grade = 11,
                chapterNumber = 8,
                title = "Purification & Characterisation of Organics",
                subBranch = "Organic Chemistry",
                subtopics = "Purification Methods: Crystallization, Distillation & Sublimation|Chromatography: Column & Thin Layer Chromatography (TLC)|Qualitative Detection of Nitrogen, Sulfur & Halogens (Lassaigne Test)|Quantitative Estimation: C & H (Liebig), N (Dumas/Kjeldahl), Halogens (Carius)"
            )
        )
        list.add(
            ChapterEntity(
                id = "C11_09",
                subject = "CHEMISTRY",
                grade = 11,
                chapterNumber = 9,
                title = "Some Basic Principles of Organic Chemistry (GOC)",
                subBranch = "Organic Chemistry",
                subtopics = "IUPAC Nomenclature of Simple & Functionalized Compounds|Structural Isomerism & Stereoisomerism (Geometrical & Optical)|Inductive Effect & Electromeric Effect|Resonance, Mesomeric Effect & Hyperconjugation|Carbocations, Carbanions, Free Radicals Stability & Electrophiles/Nucleophiles"
            )
        )
        list.add(
            ChapterEntity(
                id = "C11_10",
                subject = "CHEMISTRY",
                grade = 11,
                chapterNumber = 10,
                title = "Hydrocarbons",
                subBranch = "Organic Chemistry",
                subtopics = "Alkanes: Preparation, Conformations of Ethane/Butane & Free Radical Halogenation|Alkenes: Geometrical Isomerism, Preparation & Markovnikov/Anti-Markovnikov Addition|Ozonolysis, Hydroboration & Oxidation of Alkenes|Alkynes: Acidity of Terminal Alkynes, Addition of H2, X2, H2O|Aromaticity (Huckel's Rule) & Electrophilic Aromatic Substitution of Benzene"
            )
        )

        // ==========================================
        // JEE CHEMISTRY - CLASS 12
        // ==========================================
        list.add(
            ChapterEntity(
                id = "C12_01",
                subject = "CHEMISTRY",
                grade = 12,
                chapterNumber = 1,
                title = "Solutions",
                subBranch = "Physical Chemistry",
                subtopics = "Henry's Law & Gas Solubility in Liquids|Raoult's Law for Volatile Liquids & Ideal/Non-ideal Solutions|Relative Lowering of Vapour Pressure|Elevation in Boiling Point & Depression in Freezing Point|Osmotic Pressure, Reverse Osmosis & Van 't Hoff Factor (Association/Dissociation)"
            )
        )
        list.add(
            ChapterEntity(
                id = "C12_02",
                subject = "CHEMISTRY",
                grade = 12,
                chapterNumber = 2,
                title = "Electrochemistry",
                subBranch = "Physical Chemistry",
                subtopics = "Galvanic Cells, Standard Electrode Potentials & Electrochemical Series|Nernst Equation & Application to Cell EMF & Equilibrium Constant|Conductance, Specific Conductivity & Molar Conductivity|Kohlrausch's Law of Independent Migration of Ions|Electrolysis: Faraday's Laws & Quantitative Calculations|Batteries (Primary, Secondary), Fuel Cells & Corrosion Mechanism"
            )
        )
        list.add(
            ChapterEntity(
                id = "C12_03",
                subject = "CHEMISTRY",
                grade = 12,
                chapterNumber = 3,
                title = "Chemical Kinetics",
                subBranch = "Physical Chemistry",
                subtopics = "Average & Instantaneous Rate of Reaction|Rate Law, Rate Constant & Order vs Molecularity|Integrated Rate Equations for Zero Order & First Order Reactions|Half-life (t1/2) Determination & Graphical Analysis|Temperature Dependence of Rate: Arrhenius Equation & Activation Energy|Collision Theory of Chemical Reactions"
            )
        )
        list.add(
            ChapterEntity(
                id = "C12_04",
                subject = "CHEMISTRY",
                grade = 12,
                chapterNumber = 4,
                title = "The p-Block Elements",
                subBranch = "Inorganic Chemistry",
                subtopics = "Group 13 & 14 Electronic Configuration, Oxidation States & Inert Pair Effect|Boron Compounds: Borax, Boric Acid, Diborane Structure|Carbon Allotropes & Silicones/Silicates Overview|Group 15 Elements: Nitrogen Oxides, Ammonia & Nitric Acid Preparation|Group 16 Elements: Allotropes of Sulfur, Sulfuric Acid Contact Process|Group 17 & 18 Elements: Halogens, Interhalogens & Xenon Fluorides"
            )
        )
        list.add(
            ChapterEntity(
                id = "C12_05",
                subject = "CHEMISTRY",
                grade = 12,
                chapterNumber = 5,
                title = "The d- and f-Block Elements",
                subBranch = "Inorganic Chemistry",
                subtopics = "General Characteristics: Variable Oxidation States, Color & Catalytic Properties|Magnetic Properties: Spin-only Magnetic Moment Formula|Standard Reduction Potentials & Trends across 3d Series|Potassium Dichromate (K2Cr2O7) & Potassium Permanganate (KMnO4) Chemistry|Lanthanoids: Electronic Configuration, Oxidation States & Lanthanoid Contraction|Actinoids: Electronic Configuration & Comparison with Lanthanoids"
            )
        )
        list.add(
            ChapterEntity(
                id = "C12_06",
                subject = "CHEMISTRY",
                grade = 12,
                chapterNumber = 6,
                title = "Coordination Compounds",
                subBranch = "Inorganic Chemistry",
                subtopics = "Werner's Theory: Primary & Secondary Valencies|Coordination Number, Ligands (Monodentate, Ambidentate, Chelating)|IUPAC Nomenclature of Coordination Complexes|Isomerism: Geometrical, Optical, Ionization & Linkage Isomerism|Valence Bond Theory (VBT) for Inner/Outer Orbital Complexes|Crystal Field Theory (CFT): d-Orbital Splitting in Octahedral & Tetrahedral Fields, CFSE & Spectrochemical Series"
            )
        )
        list.add(
            ChapterEntity(
                id = "C12_07",
                subject = "CHEMISTRY",
                grade = 12,
                chapterNumber = 7,
                title = "Haloalkanes and Haloarenes",
                subBranch = "Organic Chemistry",
                subtopics = "Nomenclature & Nature of C-X Bond|Preparation Methods of Haloalkanes & Haloarenes|SN1 Mechanism: Carbocation Intermediate, Racemization & Reactivity Order|SN2 Mechanism: Inversion of Configuration & Steric Factors|Elimination Reactions (Saytzeff Rule) vs Substitution|Reactions of Haloarenes: Nucleophilic Substitution (Dow's Process) & Electrophilic Substitution"
            )
        )
        list.add(
            ChapterEntity(
                id = "C12_08",
                subject = "CHEMISTRY",
                grade = 12,
                chapterNumber = 8,
                title = "Alcohols, Phenols and Ethers",
                subBranch = "Organic Chemistry",
                subtopics = "Preparation of Alcohols: Grignard Synthesis & Hydroboration-Oxidation|Physical Properties & Acidic Character of Alcohols (Lucas Test)|Preparation of Phenols: From Cumene, Chlorobenzene & Diazonium Salts|Acidity of Phenols & Resonance Stabilization|Reactions: Kolbe's Reaction, Reimer-Tiemann Reaction & Oxidation|Ethers: Williamson Synthesis & Cleavage of C-O Bond by HI"
            )
        )
        list.add(
            ChapterEntity(
                id = "C12_09",
                subject = "CHEMISTRY",
                grade = 12,
                chapterNumber = 9,
                title = "Aldehydes, Ketones and Carboxylic Acids",
                subBranch = "Organic Chemistry",
                subtopics = "Preparation of Carbonyls: Ozonolysis, Rosenmund Reduction & Etard Reaction|Nucleophilic Addition Reactions (HCN, NaHSO3, Grignard Reagent, Ammonia derivatives)|Reactivity Comparison: Aldehydes vs Ketones|Name Reactions: Aldol Condensation, Cross-Aldol & Cannizzaro Reaction|Oxidation & Reduction: Tollens' Test, Fehling's Test, Clemmensen & Wolff-Kishner Reductions|Acidity of Carboxylic Acids & Effect of Substituents|Hell-Volhard-Zelinsky (HVZ) Reaction & Decarboxylation"
            )
        )
        list.add(
            ChapterEntity(
                id = "C12_10",
                subject = "CHEMISTRY",
                grade = 12,
                chapterNumber = 10,
                title = "Organic Compounds Containing Nitrogen (Amines)",
                subBranch = "Organic Chemistry",
                subtopics = "Classification, Nomenclature & Preparation (Gabriel Phthalimide, Hoffmann Bromamide)|Basicity of Amines in Gaseous & Aqueous Phases|Chemical Reactions: Carbylamine Test, Hinsberg Test & Nitrous Acid Reaction|Diazonium Salts: Diazotization Reaction & Stability|Synthetic Applications: Sandmeyer, Gattermann & Azo Coupling Reactions"
            )
        )
        list.add(
            ChapterEntity(
                id = "C12_11",
                subject = "CHEMISTRY",
                grade = 12,
                chapterNumber = 11,
                title = "Biomolecules",
                subBranch = "Organic & Biochemistry",
                subtopics = "Carbohydrates: Classification, D/L Configuration, Structure of Glucose & Fructose|Disaccharides (Sucrose, Lactose, Maltose) & Glycosidic Bond|Amino Acids: Zwitterion Structure & Essential vs Non-essential Amino Acids|Proteins: Peptide Linkage, Primary, Secondary (Alpha Helix, Beta Sheet), Tertiary Structure & Denaturation|Enzymes & Vitamins Classification|Nucleic Acids: Nucleosides, Nucleotides, DNA Double Helix & RNA Types"
            )
        )
        list.add(
            ChapterEntity(
                id = "C12_12",
                subject = "CHEMISTRY",
                grade = 12,
                chapterNumber = 12,
                title = "Principles Related to Practical Chemistry",
                subBranch = "Practical Chemistry",
                subtopics = "Qualitative Cation Analysis (Pb2+, Cu2+, Fe3+, Al3+, Zn2+, Ba2+, Ca2+, Mg2+, NH4+)|Qualitative Anion Analysis (CO3 2-, S 2-, SO4 2-, NO3 -, Cl-, Br-, I-)|Volumetric Analysis: Acid-Base Titrations (Phenolphthalein, Methyl Orange)|Redox Titrations: Oxalic Acid & Mohr's Salt vs KMnO4|Detection of Functional Groups: Unsaturation, Alcoholic, Phenolic, Aldehydic, Ketonic, Carboxylic & Amino"
            )
        )

        // ==========================================
        // JEE MATHEMATICS - CLASS 11
        // ==========================================
        list.add(
            ChapterEntity(
                id = "M11_01",
                subject = "MATHEMATICS",
                grade = 11,
                chapterNumber = 1,
                title = "Sets, Relations and Functions",
                subBranch = "Sets & Relations",
                subtopics = "Sets, Subsets, Power Set & Cartesian Product|Venn Diagrams & Algebraic Operations on Sets|Relations: Reflexive, Symmetric, Transitive & Equivalence Relations|Functions: Domain, Codomain, Range & Graphical Representations|One-One (Injective), Onto (Surjective), Bijective Functions & Composition"
            )
        )
        list.add(
            ChapterEntity(
                id = "M11_02",
                subject = "MATHEMATICS",
                grade = 11,
                chapterNumber = 2,
                title = "Complex Numbers & Quadratic Equations",
                subBranch = "Algebra",
                subtopics = "Algebra of Complex Numbers & Modulus-Conjugate Properties|Argand Plane, Polar Form & Euler's Formula (e^(i theta))|Square Root of Complex Number & Triangle Inequalities|Cube Roots of Unity (1, omega, omega^2) & Properties|Roots of Quadratic Equation, Nature of Roots & Discriminant|Relation between Roots and Coefficients & Symmetric Functions of Roots|Common Roots of Quadratic Equations & Location of Roots"
            )
        )
        list.add(
            ChapterEntity(
                id = "M11_03",
                subject = "MATHEMATICS",
                grade = 11,
                chapterNumber = 3,
                title = "Permutations and Combinations",
                subBranch = "Algebra",
                subtopics = "Fundamental Principles of Multiplication & Addition|Permutations of Distinct & Alike Objects (nPr)|Combinations & Selection Principles (nCr & Properties)|Circular Permutations & Restricted Arrangements|Division into Groups & Distribution of Alike/Distinct Objects|Derangements & Multinomial Theorem Concept"
            )
        )
        list.add(
            ChapterEntity(
                id = "M11_04",
                subject = "MATHEMATICS",
                grade = 11,
                chapterNumber = 4,
                title = "Binomial Theorem",
                subBranch = "Algebra",
                subtopics = "Binomial Expansion for Positive Integral Index|General Term (Tr+1) & Middle Term(s)|Greatest Term in Binomial Expansion & Numerically Greatest Term|Properties of Binomial Coefficients & Series Summations|Binomial Theorem for Any Rational Index & Approximations"
            )
        )
        list.add(
            ChapterEntity(
                id = "M11_05",
                subject = "MATHEMATICS",
                grade = 11,
                chapterNumber = 5,
                title = "Sequences and Series",
                subBranch = "Algebra",
                subtopics = "Arithmetic Progression (AP): General Term, Sum to n Terms & Properties|Geometric Progression (GP): General Term, Finite & Infinite Sum|Arithmetico-Geometric Progression (AGP) & Method of Differences|Arithmetic Mean (AM), Geometric Mean (GM) & AM >= GM Inequality|Sum of Special Series: Sigma n, Sigma n^2, Sigma n^3 & Telescopic Sums"
            )
        )
        list.add(
            ChapterEntity(
                id = "M11_06",
                subject = "MATHEMATICS",
                grade = 11,
                chapterNumber = 6,
                title = "Straight Lines",
                subBranch = "Coordinate Geometry",
                subtopics = "Slope of Line, Angle between Lines & Parallel/Perpendicular Conditions|Various Forms of Line Equations: Slope-Intercept, Point-Slope, Two-Point, Intercept, Normal|Distance of a Point from a Line & Distance between Parallel Lines|Coordinates of Centroid, Incentre, Circumcentre & Orthocentre|Family of Lines Passing through Intersection of Two Lines|Equations of Internal & External Angle Bisectors"
            )
        )
        list.add(
            ChapterEntity(
                id = "M11_07",
                subject = "MATHEMATICS",
                grade = 11,
                chapterNumber = 7,
                title = "Circles",
                subBranch = "Coordinate Geometry",
                subtopics = "Standard & General Equation of Circle & Diametric Form|Position of Point & Line with respect to Circle|Equation of Tangent (Point form, Slope form, Parametric form)|Length of Tangent, Power of a Point & Chord of Contact|Director Circle & Equation of Chord with Given Midpoint|Family of Circles & Common Tangents to Two Circles"
            )
        )
        list.add(
            ChapterEntity(
                id = "M11_08",
                subject = "MATHEMATICS",
                grade = 11,
                chapterNumber = 8,
                title = "Conic Sections (Parabola, Ellipse, Hyperbola)",
                subBranch = "Coordinate Geometry",
                subtopics = "Parabola Standard Forms (y^2 = 4ax), Focus, Directrix, Latus Rectum|Tangent to Parabola (Slope form m, Parameter t) & Normal Equations|Ellipse: Standard Equations, Eccentricity (e < 1), Foci, Directrices|Tangent to Ellipse, Director Circle & Auxiliary Circle|Hyperbola: Standard Equation, Eccentricity (e > 1), Conjugate Hyperbola|Rectangular Hyperbola (xy = c^2), Tangents & Asymptotes"
            )
        )
        list.add(
            ChapterEntity(
                id = "M11_09",
                subject = "MATHEMATICS",
                grade = 11,
                chapterNumber = 9,
                title = "Trigonometric Functions & Equations",
                subBranch = "Trigonometry",
                subtopics = "Trigonometric Functions, Domains, Ranges & Graphs|Compound Angle Formulas: sin(A+-B), cos(A+-B), tan(A+-B)|Multiple & Submultiple Angles: 2A, 3A, A/2 Formulas|Transformation of Product into Sum & Vice-Versa|Trigonometric Equations: General Solutions & Principal Values|Heights and Distances Applications"
            )
        )
        list.add(
            ChapterEntity(
                id = "M11_10",
                subject = "MATHEMATICS",
                grade = 11,
                chapterNumber = 10,
                title = "Limits and Derivatives",
                subBranch = "Calculus",
                subtopics = "Intuitive Idea of Limits & Left/Right Hand Limits|Standard Limits: Trigonometric, Exponential & Logarithmic|L'Hopital's Rule for 0/0 and Infinity/Infinity Forms|First Principle of Differentiation|Derivative of Polynomial, Trigonometric & Rational Functions"
            )
        )
        list.add(
            ChapterEntity(
                id = "M11_11",
                subject = "MATHEMATICS",
                grade = 11,
                chapterNumber = 11,
                title = "Mathematical Reasoning & Linear Inequalities",
                subBranch = "Reasoning & Algebra",
                subtopics = "Statements, Connectives (And, Or, If-then, If and only if)|Negation, Converse, Contrapositive & Inverse of Statements|Tautology & Contradiction Truth Tables|Linear Inequalities in One & Two Variables & Graphical Solution"
            )
        )
        list.add(
            ChapterEntity(
                id = "M11_12",
                subject = "MATHEMATICS",
                grade = 11,
                chapterNumber = 12,
                title = "Statistics",
                subBranch = "Statistics",
                subtopics = "Measures of Central Tendency: Mean, Median & Mode|Measures of Dispersion: Mean Deviation about Mean & Median|Variance and Standard Deviation for Ungrouped & Grouped Data|Analysis of Frequency Distributions & Coefficient of Variation"
            )
        )

        // ==========================================
        // JEE MATHEMATICS - CLASS 12
        // ==========================================
        list.add(
            ChapterEntity(
                id = "M12_01",
                subject = "MATHEMATICS",
                grade = 12,
                chapterNumber = 1,
                title = "Matrices",
                subBranch = "Algebra",
                subtopics = "Types of Matrices & Matrix Algebra (Addition, Scalar Multiplication)|Matrix Multiplication & Properties (Non-commutativity)|Transpose of Matrix, Symmetric & Skew-Symmetric Matrices|Orthogonal Matrices & Invertible Matrices|Elementary Row Operations & Finding Inverse using Gauss-Jordan"
            )
        )
        list.add(
            ChapterEntity(
                id = "M12_02",
                subject = "MATHEMATICS",
                grade = 12,
                chapterNumber = 2,
                title = "Determinants",
                subBranch = "Algebra",
                subtopics = "Evaluation of Determinants of Order 2 and 3|Minors and Cofactors of Elements|Properties of Determinants & Simplification Techniques|Adjoint and Inverse of a Square Matrix & Properties|Cramer's Rule for Solving System of Linear Equations|Consistency & Inconsistency of Linear Equations using Matrix Inverse"
            )
        )
        list.add(
            ChapterEntity(
                id = "M12_03",
                subject = "MATHEMATICS",
                grade = 12,
                chapterNumber = 3,
                title = "Inverse Trigonometric Functions",
                subBranch = "Trigonometry",
                subtopics = "Principal Value Branches, Domains & Ranges of Inverse Trig Functions|Graphs of sin^-1(x), cos^-1(x), tan^-1(x) etc.|Fundamental Identities: sin^-1(x) + cos^-1(x) = pi/2|Addition & Subtraction Formulas for tan^-1(x) +- tan^-1(y)|Transformations & Equations involving Inverse Trigonometric Functions"
            )
        )
        list.add(
            ChapterEntity(
                id = "M12_04",
                subject = "MATHEMATICS",
                grade = 12,
                chapterNumber = 4,
                title = "Continuity and Differentiability",
                subBranch = "Calculus",
                subtopics = "Continuity at a Point, in an Interval & Discontinuity Types|Intermediate Value Theorem & Algebra of Continuous Functions|Differentiability at a Point & Geometrical Meaning|Derivatives using Chain Rule, Inverse Trig & Implicit Functions|Logarithmic Differentiation & Parametric Differentiation|Second & Higher Order Derivatives"
            )
        )
        list.add(
            ChapterEntity(
                id = "M12_05",
                subject = "MATHEMATICS",
                grade = 12,
                chapterNumber = 5,
                title = "Application of Derivatives (AOD)",
                subBranch = "Calculus",
                subtopics = "Rate of Change of Quantities & Related Rates|Equation of Tangents and Normals to Curves|Angle of Intersection between Two Curves (Orthogonal Curves)|Monotonicity: Increasing & Decreasing Functions (First Derivative Test)|Maxima and Minima: Local Extrema, Second Derivative Test & Absolute Extrema|Optimization Word Problems in Geometry & Physics|Rolle's Theorem & Lagrange's Mean Value Theorem (LMVT)"
            )
        )
        list.add(
            ChapterEntity(
                id = "M12_06",
                subject = "MATHEMATICS",
                grade = 12,
                chapterNumber = 6,
                title = "Indefinite Integrals",
                subBranch = "Calculus",
                subtopics = "Standard Integrals of Algebraic, Trigonometric, Exponential & Logarithmic Functions|Integration by Substitution Method|Integration using Trigonometric Identities & Formulae|Integration by Parts & Special Forms like e^x [f(x) + f'(x)]|Integration by Partial Fractions (Linear & Quadratic Factors)|Standard Quadratic Integrals: 1/(ax^2+bx+c), 1/sqrt(ax^2+bx+c)"
            )
        )
        list.add(
            ChapterEntity(
                id = "M12_07",
                subject = "MATHEMATICS",
                grade = 12,
                chapterNumber = 7,
                title = "Definite Integrals",
                subBranch = "Calculus",
                subtopics = "Definite Integral as Limit of a Sum|Fundamental Theorem of Calculus (Evaluation)|Properties of Definite Integrals (King's Property, Periodicity, Even/Odd)|Leibniz Rule for Differentiation under Integral Sign|Wallis Formula & Definite Integral Estimations"
            )
        )
        list.add(
            ChapterEntity(
                id = "M12_08",
                subject = "MATHEMATICS",
                grade = 12,
                chapterNumber = 8,
                title = "Application of Integrals (Area under Curves)",
                subBranch = "Calculus",
                subtopics = "Area Bounded by a Curve and Coordinate Axes|Area Bounded between Two Curves (Parabola & Line, Circle & Line)|Area with Symmetry & Shaded Geometric Regions"
            )
        )
        list.add(
            ChapterEntity(
                id = "M12_09",
                subject = "MATHEMATICS",
                grade = 12,
                chapterNumber = 9,
                title = "Differential Equations",
                subBranch = "Calculus",
                subtopics = "Order and Degree of Differential Equations|Formation of Differential Equation from Family of Curves|Variable Separable Method & Reducible Forms|Homogeneous Differential Equations & Substitutions (y = vx)|Linear Differential Equations of First Order & Integrating Factor (IF)|Orthogonal Trajectories & Application to Growth/Decay Models"
            )
        )
        list.add(
            ChapterEntity(
                id = "M12_10",
                subject = "MATHEMATICS",
                grade = 12,
                chapterNumber = 10,
                title = "Vector Algebra",
                subBranch = "Vectors & 3D",
                subtopics = "Position Vectors, Direction Cosines & Direction Ratios|Scalar (Dot) Product of Vectors & Projections|Vector (Cross) Product of Vectors & Geometrical Interpretation|Scalar Triple Product (Box Product [a b c]) & Coplanarity Condition|Vector Triple Product: a x (b x c) Expansion & Applications"
            )
        )
        list.add(
            ChapterEntity(
                id = "M12_11",
                subject = "MATHEMATICS",
                grade = 12,
                chapterNumber = 11,
                title = "Three Dimensional Geometry (3D)",
                subBranch = "Vectors & 3D",
                subtopics = "Direction Cosines & Direction Ratios of Line in Space|Vector & Cartesian Equation of a Line Passing through Given Points|Angle between Two Lines & Conditions for Parallel/Perpendicular Lines|Shortest Distance between Two Skew Lines & Intersecting Lines|Vector & Cartesian Equation of a Plane|Angle between Two Planes & Distance of a Point from a Plane|Line of Intersection of Two Planes & Coplanar Lines"
            )
        )
        list.add(
            ChapterEntity(
                id = "M12_12",
                subject = "MATHEMATICS",
                grade = 12,
                chapterNumber = 12,
                title = "Probability",
                subBranch = "Probability",
                subtopics = "Conditional Probability & Multiplication Theorem|Independent Events & Mutually Exclusive Events|Law of Total Probability & Bayes' Theorem|Random Variable, Probability Distribution & Expectation (Mean)|Variance and Standard Deviation of Random Variable|Bernoulli Trials and Binomial Distribution B(n, p)"
            )
        )

        return list
    }
}
